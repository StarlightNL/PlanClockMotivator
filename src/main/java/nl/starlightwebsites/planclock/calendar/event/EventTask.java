package nl.starlightwebsites.planclock.calendar.event;

import com.google.api.services.calendar.model.Event;
import nl.starlightwebsites.planclock.Main;

import java.util.TimerTask;

public class EventTask extends TimerTask {
    static String speechFormatNoPlace = "This is a reminder that in %s you have the appointment %s";
    static String speechFormatPlace = speechFormatNoPlace + "at the location %s";
    int reminderMinutes = 0;
    Event event;

    public EventTask(Event event, int reminderMinutes){
        this.event = event;
        this.reminderMinutes = reminderMinutes;
    }

    @Override
    public void run() {
        String timeNotation;
        if(reminderMinutes % 1440 == 0){
            if(reminderMinutes == 1440){
                timeNotation = "1 day";
            } else{
                timeNotation = reminderMinutes / 1440 + " days";
            }
        }
        else if(reminderMinutes % 60 == 0){
            if(reminderMinutes == 60) {
                timeNotation = "1 hour";
            } else{
                timeNotation = reminderMinutes / 60 + " hours";
            }
        }
        if(reminderMinutes == 30){
            timeNotation = "half an hour";
        } else{
            timeNotation = reminderMinutes + " minutes";
        }
        if(event.getLocation() == null) {
            Main.tts.say(String.format(speechFormatNoPlace, timeNotation, event.getSummary()));
        } else{
            Main.tts.say(String.format(speechFormatNoPlace, timeNotation, event.getSummary(), event.getLocation()));
        }
    }
}
