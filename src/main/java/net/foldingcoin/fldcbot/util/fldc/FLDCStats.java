package net.foldingcoin.fldcbot.util.fldc;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.foldingcoin.fldcbot.BotLauncher;
import net.foldingcoin.fldcbot.util.distribution.DistributionUtils;

public class FLDCStats {

    private static final Gson GSON = new GsonBuilder().create();

    private static final String BASE_URL = "http://foldingcoin.xyz/getdata.php?date=";

    private static final File FLDC_DIR = new File(BotLauncher.DATA_DIR, "fldc");
    private static final String FILE_PREV_DISTRIBUTION = "distribution_future.json";
    private static final String FILE_CURRENT_DISTRIBUTION = "distribution_last.json";
    private static final String FILE_YESTERDAY_DISTRIBUTION = "distribution_yesterday.json";

    public static Map<String, FLDCUser> distributionsFuture = new TreeMap<>();
    public static Map<String, FLDCUser> distributionsPast = new TreeMap<>();
    public static Map<String, FLDCUser> distributionsYesterday = new TreeMap<>();
    public static Map<String, FLDCUser> distributionsDifference = new TreeMap<>();

    public static long totalPoints = 0;
    public static long pastPoints = 0;
    public static long futurePoints = 0;
    public static long differencePoints = 0;
    public static long yesterdayPoints = 0;
    
    public static int activeFolders = 0;
    

    /**
     * Downloads the unpaid FLDC data and collects it for use.
     */
    public static void reload () {
        distributionsFuture.clear();
        distributionsPast.clear();
        distributionsYesterday.clear();
        distributionsDifference.clear();
        totalPoints = 0;
        pastPoints = 0;
        futurePoints = 0;
        differencePoints = 0;
        yesterdayPoints = 0;
        final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Downloads the files
        BotLauncher.instance.downloadFile(BASE_URL + dateFormat.format(LocalDate.now(ZoneId.of("America/Los_Angeles"))), FLDC_DIR, FILE_PREV_DISTRIBUTION);
        BotLauncher.instance.downloadFile(BASE_URL + dateFormat.format(LocalDate.now(ZoneId.of("America/Los_Angeles")).minusDays(1)), FLDC_DIR, FILE_YESTERDAY_DISTRIBUTION);
        BotLauncher.instance.downloadFile(BASE_URL + dateFormat.format(DistributionUtils.getLastDistribution()), FLDC_DIR, FILE_CURRENT_DISTRIBUTION);

        try (FileReader reader = new FileReader(new File(FLDC_DIR, FILE_YESTERDAY_DISTRIBUTION))) {
            distributionsYesterday = mapUsers(GSON.fromJson(reader, FLDCUser[].class));
        }
        catch (final IOException e) {
            BotLauncher.LOG.error(FILE_YESTERDAY_DISTRIBUTION + " was not found!", e);
        }
        yesterdayPoints = totalPoints;
        totalPoints = 0;

        try (FileReader reader = new FileReader(new File(FLDC_DIR, FILE_PREV_DISTRIBUTION))) {
            distributionsFuture = mapUsers(GSON.fromJson(reader, FLDCUser[].class));
        }
        catch (final IOException e) {
            BotLauncher.LOG.error(FILE_PREV_DISTRIBUTION + " was not found!", e);
        }
        // calculates the future points
        futurePoints = totalPoints;
        try (FileReader reader = new FileReader(new File(FLDC_DIR, FILE_CURRENT_DISTRIBUTION))) {
            distributionsPast = mapUsers(GSON.fromJson(reader, FLDCUser[].class));
        }
        catch (final IOException e) {
            BotLauncher.LOG.error(FILE_CURRENT_DISTRIBUTION + " was not found!", e);
        }

        // Calculates the past points
        pastPoints = totalPoints - futurePoints;
        differencePoints = futurePoints - pastPoints;

        // Loops over the users and creates a new user that has the difference between the old
        // and new user.
        for (final String key : distributionsFuture.keySet()) {
            final FLDCUser user_future = distributionsFuture.get(key);
            final FLDCUser user_past = distributionsPast.get(key);
            // There may not always be a past user
            final long oldCred = user_past != null ? user_past.getNewCredit() : 0;
            final FLDCUser user_diff = new FLDCUser(user_future.getId(), user_future.getName(), user_future.getToken(), user_future.getAddress(), user_future.getNewCredit() - oldCred);
            distributionsDifference.put(key, user_diff);
            if(user_diff.getNewCredit()>0){
                activeFolders++;
            }
        }
        
        if(!BotLauncher.instance.getConfig().getWebDir().isEmpty()){
            try {
                File webDir = new File(BotLauncher.instance.getConfig().getWebDir(), "fldcppd.json");
                BufferedWriter writer = new BufferedWriter(new FileWriter(webDir));
                FLDCApi api = new FLDCApi(getTeamPPD(), activeFolders, getTeamPPD()/activeFolders, distributionsDifference.size());
                writer.write(GSON.toJson(api, FLDCApi.class));
                writer.close();
            } catch(IOException e) {
                e.printStackTrace();
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

        for (final FLDCUser user : users) {

            // Currently only users who are using all or fldc are tracked.
            if (user.getToken().equalsIgnoreCase("all") || user.getToken().equalsIgnoreCase("fldc")) {

                totalPoints += user.getNewCredit();
                map.put(String.format("%s_%s_%s", user.getName(), user.getToken(), user.getAddress()), user);
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
            if (entry.getKey().startsWith(key)) {
                return entry.getValue();
            }
            else if (entry.getValue().getAddress().equals(key)) {
                return entry.getValue();
            }
            else if (key.equalsIgnoreCase(entry.getValue().getId() + "")) {
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
            if (entry.getKey().startsWith(key)) {
                users.add(entry.getValue());
            }
            else if (entry.getValue().getAddress().equals(key)) {
                users.add(entry.getValue());
            }
            else if (key.equalsIgnoreCase(entry.getValue().getId() + "")) {
                users.add(entry.getValue());
            }
        }
        return users;
    }

}
