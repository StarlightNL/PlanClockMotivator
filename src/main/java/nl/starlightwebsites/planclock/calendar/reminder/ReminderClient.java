package nl.starlightwebsites.planclock.calendar.reminder;

import com.google.api.client.http.*;
import nl.starlightwebsites.planclock.Main;
import nl.starlightwebsites.planclock.database.DBMotivation;
import nl.starlightwebsites.planclock.database.DBReminder;
import nl.starlightwebsites.planclock.calendar.reminder.model.raw.ResponseReminder;
import nl.starlightwebsites.planclock.database.DBTimePlanning;
import nl.starlightwebsites.planclock.models.Reminder;
import nl.starlightwebsites.planclock.models.TimePlanning;
import org.tinylog.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static nl.starlightwebsites.planclock.helpers.GoogleAuthenticate.credentials;
import static nl.starlightwebsites.planclock.helpers.GoogleAuthenticate.httpTransport;

public class ReminderClient {


    /**
     * All timer planed reminders
     */
    public static ConcurrentHashMap<Integer, Reminder> plannedReminders = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<Integer, Reminder> reminders = new ConcurrentHashMap<>();

    private static HashMap<String, String> URIs = new HashMap<>(){{
        put("create","https://reminders-pa.clients6.google.com/v1internalOP/reminders/create");
        put("delete", "https://reminders-pa.clients6.google.com/v1internalOP/reminders/delete");
        put("get", "https://reminders-pa.clients6.google.com/v1internalOP/reminders/get");
        put("list", "https://reminders-pa.clients6.google.com/v1internalOP/reminders/list");
    }};

    public static void startPollingReminders(){
        Main.globalTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                ReminderClient.getReminders();
            }
        }, Date.from(Instant.now()), Duration.of(1, ChronoUnit.HOURS).toMillis()); // Check every hour for new reminders
    }

    public static List<Reminder> getReminders(){
        HashMap<Integer, Reminder> remindersToParse = new HashMap<>();
        List<Reminder> dbReminders = DBReminder.getAllReminders();
        for(Reminder reminder : dbReminders){
            remindersToParse.put(reminder.getDbid(), reminder);
        }

        HttpResponse response;
        try {
            HttpRequest request = httpTransport.createRequestFactory(credentials)
                    .buildPostRequest(new GenericUrl(URIs.get("list")),
                            ByteArrayContent.fromString("application/json+protobuf", ReminderClientUtils.createListRequestBody(20, Instant.now().plus(4, ChronoUnit.DAYS))));
            request.getHeaders().setContentType("application/json+protobuf");
            response = request.execute();
        } catch (IOException e) {
            Logger.error(e, "Failed to get reminders");
            return new ArrayList<>(reminders.values());
        }
        if(response.getStatusCode() != 200){
            Logger.error("Invalid response.");
        } else{
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getContent()));
                String result = reader.readLine(); // We only get one string
                var remindersMap = Main.json.readValue(result, ResponseReminder.class);
                String remindersNice = Main.jsonIgnoreAn.writeValueAsString(remindersMap);

                for(var r : remindersMap.reminders){
                    String title = r.title;
                    String id = r._1.id.replace("/","_");
                    int year = r.date.year;
                    int month = r.date.month;
                    int day = r.date.day;
                    int hour = r.date.time.hour;
                    int minute = r.date.time.minute;
                    int second = r.date.time.second;
                    Reminder reminder = new Reminder(id, title, year, month, day, hour, minute, second);
                    reminder.setGoogleReminder(true);
                    if(!reminder.isBeforeNow()) {
                        Reminder dbReminder = DBReminder.getReminderByTitle(title);
                        if(dbReminder == null){
                            dbReminder = DBReminder.addReminder(title);
                            reminder.setDbid(dbReminder.getDbid());
                            remindersToParse.put(reminder.getDbid(), reminder);

                            List<TimePlanning> tps = DBTimePlanning.getAllTimePlannings();

                            TimePlanning tp = new TimePlanning();
                            tp.setPlannedTimeWithInstant(reminder.getPlannedTime());
                            if(r.repeatSchedule != null) {
                                try {
                                    tp.setWeeksBetween(r.repeatSchedule._1.repeatInterval);
                                } catch (NullPointerException ignored){}
                                try {
                                    tp.setDayBitmaskWithDayNumbers(r.repeatSchedule._1.daysOfTheWeek._1);
                                } catch (NullPointerException ignored) {
                                    if(r.repeatSchedule._1.repeatIntervalType == 0) {
                                        tp.setDayBitmask(127); // This is all days
                                        tp.setWeeksBetween(0);
                                    } else{
                                        System.out.println("Unhandled condition");
                                    }
                                    // TODO: Go to bed triggers this but is repeating. What is the difference here?
                                }
                            }

                            boolean tpExists = false;
                            for(TimePlanning dbtp : tps){
                                if(tp.equals(dbtp)){
                                    tpExists = true;
                                }
                            }
                            if(!tpExists){
                                Logger.debug("Adding timeplanning {} because it does not exist yet",tp);
                                tp = DBTimePlanning.addTimePlanning(tp);
                                DBTimePlanning.bindPlanningToReminder(tp,reminder);
                                tps.add(tp);
                            }
                            remindersToParse.put(reminder.getDbid(), reminder);
                        } else{
                            reminder.setDbid(dbReminder.getDbid());
                            if(reminder.getDbid() == 0){
                                Logger.debug("Uhm... dbid is 0 this cannot happen");
                            } else {
                                remindersToParse.put(reminder.getDbid(), reminder);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                Logger.error(e,"Failed to get response reader");
            }
        }
        for(Reminder reminder : remindersToParse.values()){
            List<TimePlanning> tps = DBTimePlanning.getAllTimePlanningsByReminderId(reminder.getDbid());
            reminder.addMotivations(DBMotivation.getMotivationsByReminderId(reminder.getDbid()));
            reminder.addTimePlannings(tps);
            if(reminder.getDbid() == 0){
                Logger.error("Something went wrong");
            } else {
                reminders.put(reminder.getDbid(), reminder);
            }
        }
        planReminders(); // This can always be done because if it is planned. It is not replanned
        return new ArrayList<>(reminders.values());
    }

    public static void planReminders(){
        if(true){
            Logger.error(new Throwable("REMOVE THE IF TRUE FROM HERE"),"DO NOT FORGET");
            return;
        }
        for(var reminderkv : reminders.entrySet()){
            if(plannedReminders.containsKey(reminderkv.getKey())) continue;
            plannedReminders.put(reminderkv.getKey(), reminderkv.getValue());
            Main.calenderTimer.schedule(new ReminderTask(reminderkv.getValue(), true), Date.from(reminderkv.getValue().getPlannedTime().minus(10, ChronoUnit.MINUTES))); // 10 minute notification mark
            Main.calenderTimer.schedule(new ReminderTask(reminderkv.getValue()), Date.from(reminderkv.getValue().getPlannedTime())); // Main notification
        }
    }
}
