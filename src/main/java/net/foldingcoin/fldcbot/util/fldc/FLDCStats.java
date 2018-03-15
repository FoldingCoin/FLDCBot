package net.foldingcoin.fldcbot.util.fldc;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import net.foldingcoin.fldcbot.BotLauncher;

public class FLDCStats {

    private static final Gson GSON = new GsonBuilder().create();

    private static final String BASE_URL = "http://foldingcoin.xyz/getdata.php?date=";

    private static final File FLDC_DIR = new File(BotLauncher.DATA_DIR, "fldc");
    private static final String FILE_PREV_DISTRIBUTION = "distribution_future.json";
    private static final String FILE_CURRENT_DISTRIBUTION = "distribution_last.json";

    public static Map<String, FLDCUser> distributionsFuture = new TreeMap<>();
    public static Map<String, FLDCUser> distributionsPast = new TreeMap<>();
    public static Map<String, FLDCUser> distributionsDifference = new TreeMap<>();

    public static long totalPoints = 0;
    public static long pastPoints = 0;
    public static long futurePoints = 0;
    public static long differencePoints = 0;

    /**
     * Downloads the unpaid FLDC data and collects it for use.
     */
    public static void init () {

        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final Calendar instance = Calendar.getInstance();
        // Downloads the files
        BotLauncher.instance.downloadFile(BASE_URL + dateFormat.format(instance.getTime()), FLDC_DIR, FILE_PREV_DISTRIBUTION);
        BotLauncher.instance.downloadFile(BASE_URL + dateFormat.format(getFirstSaturday(instance)), FLDC_DIR, FILE_CURRENT_DISTRIBUTION);

        try (FileReader reader = new FileReader(new File(FLDC_DIR, FILE_PREV_DISTRIBUTION))) {
            distributionsFuture = getUsers(GSON.fromJson(reader, List.class));
        }
        catch (final IOException e) {
            BotLauncher.LOG.error(FILE_PREV_DISTRIBUTION + " was not found!", e);
        }
        // calculates the future points
        futurePoints = totalPoints;
        try (FileReader reader = new FileReader(new File(FLDC_DIR, FILE_CURRENT_DISTRIBUTION))) {
            distributionsPast = getUsers(GSON.fromJson(reader, List.class));
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
        }

    }

    /**
     * Processes users from the JSON files, users' who's "token" isn't "all" or "fldc" are
     * ignored.
     *
     * @param users List of json objects
     *
     * @return Map of <Name, FLDC> containing all the users from the given list.
     */
    private static Map<String, FLDCUser> getUsers (List<LinkedTreeMap> users) {

        final Map<String, FLDCUser> map = new TreeMap<>();
        for (final LinkedTreeMap ent : users) {
            final FLDCUser user = new FLDCUser();
            user.setId(Long.parseLong((String) ent.get("id")));
            user.setName(((String) ent.get("name")).toLowerCase());
            user.setToken((String) ent.get("token"));
            user.setAddress((String) ent.get("address"));
            user.setNewCredit(Long.parseLong((String) ent.get("newcredit")));
            if (user.getToken().equalsIgnoreCase("all") || user.getToken().equalsIgnoreCase("fldc")) {
                totalPoints += user.getNewCredit();
                map.put(user.getName(), user);
            }
        }
        return map;
    }

    /**
     * Gets the date of the first Saturday of the month, if the month first Saturday has
     * passed, the first Saturday of the next month will be returned.
     *
     * @param calendar Current date
     *
     * @return The first Saturday of the month.
     */
    public static Date getFirstSaturday (Calendar calendar) {

        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
        cal.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        cal.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        if (cal.get(Calendar.DATE) < calendar.get(Calendar.DATE)) {
            cal.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1 >= Calendar.JANUARY ? calendar.get(Calendar.MONTH) - 1 : Calendar.DECEMBER);
            return getFirstSaturday(cal);
        }
        return cal.getTime();
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

        if (map.containsKey(key)) {
            return map.get(key);
        }
        else {
            for (final Map.Entry<String, FLDCUser> entry : map.entrySet()) {
                if (entry.getValue().getAddress().equals(key)) {
                    return entry.getValue();
                }
                else if (key.equalsIgnoreCase(entry.getValue().getId() + "")) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

}
