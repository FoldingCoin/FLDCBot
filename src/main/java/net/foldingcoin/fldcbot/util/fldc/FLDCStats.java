package net.foldingcoin.fldcbot.util.fldc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import net.foldingcoin.fldcbot.BotLauncher;
import net.foldingcoin.fldcbot.util.distribution.DistributionUtils;

/**
 * Downloads data from the Foldingcoin.xyz api and aggregates it into a usable format.
 */
public final class FLDCStats {
    
    private static final Gson GSON = new GsonBuilder().create();
    
    private static final String BASE_URL = "http://foldingcoin.xyz/getdata.php?date=";
    
    private static final File FLDC_DIR = new File(BotLauncher.DATA_DIR, "fldc");
    private static final String FILE_PREV_DISTRIBUTION = "distribution_future.json";
    private static final String FILE_CURRENT_DISTRIBUTION = "distribution_last.json";
    private static final String FILE_YESTERDAY_DISTRIBUTION = "distribution_yesterday.json";
    
    private static Map<String, FLDCUser> distributionsFuture = new TreeMap<>();
    private static Map<String, FLDCUser> distributionsPast = new TreeMap<>();
    private static Map<String, FLDCUser> distributionsYesterday = new TreeMap<>();
    private static Map<String, FLDCUser> distributionsDifference = new TreeMap<>();
    
    private static long totalPoints = 0;
    private static long futurePoints = 0;
    private static long differencePoints = 0;
    private static long yesterdayPoints = 0;
    private static int activeFolders = 0;
    
    public static final ZoneId ZONE_ID = ZoneId.of("America/Los_Angeles");
    
    /**
     * Utility classes, such as this one, are not meant to be instantiated. Java adds an
     * implicit public constructor to every class which does not define at lease one
     * explicitly. Hence why this constructor was added.
     */
    private FLDCStats () {
        
        throw new IllegalAccessError("Utility class");
    }
    
    /**
     * Downloads the unpaid FLDC data and collects it for use.
     */
    public static void reload () {
        
        distributionsFuture.clear();
        distributionsPast.clear();
        distributionsYesterday.clear();
        distributionsDifference.clear();
        totalPoints = 0;
        futurePoints = 0;
        differencePoints = 0;
        yesterdayPoints = 0;
        long pastPoints = 0;
        final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        // Downloads the files
        
        BotLauncher.instance.downloadFile(BASE_URL + dateFormat.format(LocalDate.now(ZONE_ID)), FLDC_DIR, FILE_PREV_DISTRIBUTION);
        BotLauncher.instance.downloadFile(BASE_URL + dateFormat.format(LocalDate.now(ZONE_ID).minusDays(1)), FLDC_DIR, FILE_YESTERDAY_DISTRIBUTION);
        BotLauncher.instance.downloadFile(BASE_URL + dateFormat.format(DistributionUtils.getFirstDay(YearMonth.of(LocalDate.now(ZONE_ID).getYear(), LocalDate.now(ZONE_ID).getMonth()))), FLDC_DIR, FILE_CURRENT_DISTRIBUTION);
        
        try (FileReader reader = new FileReader(new File(FLDC_DIR, FILE_YESTERDAY_DISTRIBUTION))) {
            distributionsYesterday = mapUsers(GSON.fromJson(reader, FLDCUser[].class));
        } catch (final IOException e) {
            BotLauncher.LOG.error(FILE_YESTERDAY_DISTRIBUTION + " was not found!", e);
        }
        yesterdayPoints = totalPoints;
        totalPoints = 0;
        
        try (FileReader reader = new FileReader(new File(FLDC_DIR, FILE_PREV_DISTRIBUTION))) {
            distributionsFuture = mapUsers(GSON.fromJson(reader, FLDCUser[].class));
        } catch (final IOException e) {
            BotLauncher.LOG.error(FILE_PREV_DISTRIBUTION + " was not found!", e);
        }
        // calculates the future points
        futurePoints = totalPoints;
        try (FileReader reader = new FileReader(new File(FLDC_DIR, FILE_CURRENT_DISTRIBUTION))) {
            distributionsPast = mapUsers(GSON.fromJson(reader, FLDCUser[].class));
        } catch (final IOException e) {
            BotLauncher.LOG.error(FILE_CURRENT_DISTRIBUTION + " was not found!", e);
        }
        
        // Calculates the past points
        pastPoints = totalPoints - futurePoints;
        differencePoints = futurePoints - pastPoints;
        
        // Loops over the users and creates a new user that has the difference between
        // the old and new user.
        for (final String key : distributionsFuture.keySet()) {
            final FLDCUser userFuture = distributionsFuture.get(key);
            final FLDCUser userPast = distributionsPast.get(key);
            // There may not always be a past user
            final long oldCred = userPast != null ? userPast.getNewCredit() : 0;
            final FLDCUser userDiff = new FLDCUser(userFuture.getId(), userFuture.getName(), userFuture.getToken(), userFuture.getAddress(), userFuture.getNewCredit() - oldCred);
            distributionsDifference.put(key, userDiff);
            if (userDiff.getNewCredit() > 0) {
                activeFolders++;
            }
        }
        
    }
    
    public static long getTeamPPD () {
        
        return futurePoints - yesterdayPoints;
    }
    
    /**
     * Maps all of the users to a name so they can be looked up at a later time. This method is
     * also responsible for updating {@link #totalPoints}.
     *
     * @param users An array of users to account for.
     * @return A map of all users mapped to their name.
     */
    private static Map<String, FLDCUser> mapUsers (FLDCUser[] users) {
        
        final Map<String, FLDCUser> map = new HashMap<>();
        
        if(users == null){
            return map;
        }
        
        for (final FLDCUser user : users) {
            
            // Currently only users who are using all or fldc are tracked.
            if (user.getToken().equalsIgnoreCase("all") || user.getToken().equalsIgnoreCase("fldc")) {
                
                totalPoints += user.getNewCredit();
                String key = String.format("%s_%s_%s", user.getName(), user.getToken(), user.getAddress());
                
                if (map.containsKey(key)) {
                    final FLDCUser combined = new FLDCUser(user.getId(), user.getName(), user.getToken(), user.getAddress(), user.getNewCredit() + map.get(key).getNewCredit());
                    map.put(key, combined);
                }
                else {
                    
                    map.put(key, user);
                }
            }
        }
        
        return map;
    }
    
    public static FLDCUser getFutureUser (String key) {
        
        return getUser(distributionsFuture, key);
    }
    
    public static FLDCUser getPastUser (String key) {
        
        return getUser(distributionsPast, key);
    }
    
    public static FLDCUser getDifferenceUser (String key) {
        
        return getUser(distributionsDifference, key);
    }
    
    public static FLDCUser getUser (Map<String, FLDCUser> map, String key) {
        
        for (final Map.Entry<String, FLDCUser> entry : map.entrySet()) {
            
            if (entry.getKey().startsWith(key) || entry.getValue().getAddress().equals(key) || key.equalsIgnoreCase(entry.getValue().getId() + "")) {
                
                return entry.getValue();
            }
        }
        
        return null;
    }
    
    public static List<FLDCUser> getFutureUsers (String key) {
        
        return getUsers(distributionsFuture, key);
    }
    
    public static List<FLDCUser> getPastUsers (String key) {
        
        return getUsers(distributionsPast, key);
    }
    
    public static List<FLDCUser> getDifferenceUsers (String key) {
        
        return getUsers(distributionsDifference, key);
    }
    
    public static List<FLDCUser> getUsers (Map<String, FLDCUser> map, String key) {
        
        final List<FLDCUser> users = new LinkedList<>();
        for (final Map.Entry<String, FLDCUser> entry : map.entrySet()) {
            if (entry.getKey().startsWith(key) || entry.getValue().getAddress().equals(key) || key.equalsIgnoreCase(entry.getValue().getId() + "")) {
                users.add(entry.getValue());
            }
        }
        return users;
    }
    
    public static long getNewPoints () {
        
        return differencePoints;
    }
    
    public static int getTotalFolders () {
        
        return distributionsDifference.size();
    }
    
    public static int getActiveFolders () {
        
        return activeFolders;
    }
}
