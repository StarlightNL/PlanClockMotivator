package nl.starlightwebsites.planclock.calendar.event;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import nl.starlightwebsites.planclock.Main;
import nl.starlightwebsites.planclock.calendar.CalendarUtil;
import org.tinylog.Logger;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EventClient {


    /**
     * All timer planed reminders
     */
    public static ConcurrentHashMap<String, Event> plannedEvents = new ConcurrentHashMap<>();
    /**
     * All reminders
     */
    public static ConcurrentHashMap<String, Event> events = new ConcurrentHashMap<>();

    public static void startPollingEvents(){
        Main.globalTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                EventClient.getEvents();
            }
        }, Date.from(Instant.now()), Duration.of(1, ChronoUnit.HOURS).toMillis()); // Check every hour for new reminders
    }

    public static List<Event> getEvents(){
        Events cevents = null;
        try {
            cevents = CalendarUtil.calendar.events().list("primary")
                    .setTimeMin(new DateTime(Date.from(Instant.now())))
                    .setTimeMax(new DateTime(Date.from(Instant.now().plus(3, ChronoUnit.DAYS))))
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
        } catch (IOException e) {
            Logger.error(e,"Failed to get events from primary calendar");
        }
        List<Event> items = cevents.getItems();
        if (!items.isEmpty()) {
            for (Event event : items) {
                events.put(event.getId(), event);
            }
            planEvents();
        }

        return items;
    }

    public static ConcurrentHashMap<String, Event> getCachedEvents(){
        return events;
    }

    public static void planEvents(){
        for(var eventKV : events.entrySet()){
            if(plannedEvents.containsKey(eventKV.getKey())){
                Instant startTime = Instant.ofEpochMilli(eventKV.getValue().getStart().getDateTime().getValue()).plus(eventKV.getValue().getStart().getDateTime().getTimeZoneShift(), ChronoUnit.MINUTES);
                if(startTime.isBefore(Instant.now())) {
                    Logger.debug("Removing old event");
                    events.remove(eventKV.getKey());
                    plannedEvents.remove(eventKV.getKey());
                }
                continue;
            }
            plannedEvents.put(eventKV.getKey(), eventKV.getValue());
            if(eventKV.getValue().getReminders().getUseDefault()){
                List<Integer> plannedMinutes = new ArrayList<>();
                for(var defaultReminder : CalendarUtil.defaultReminders){
                    if(plannedMinutes.contains(defaultReminder.getMinutes())) continue;
                    plannedMinutes.add(defaultReminder.getMinutes());
                    Instant scheduleFor = Instant.ofEpochMilli(eventKV.getValue().getStart().getDateTime().getValue()).plus(eventKV.getValue().getStart().getDateTime().getTimeZoneShift(), ChronoUnit.MINUTES).minus(defaultReminder.getMinutes(), ChronoUnit.MINUTES);
                    if(scheduleFor.isBefore(Instant.now())) continue; // No use to plan notifications in the past
                    Main.calenderTimer.schedule(new EventTask(eventKV.getValue(), defaultReminder.getMinutes()), Date.from(scheduleFor));
                }
            } else{
                System.out.println("HANDLE THIS SENNA");
            }
        }

    }
}
