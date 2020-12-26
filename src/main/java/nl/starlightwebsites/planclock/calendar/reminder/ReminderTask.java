package nl.starlightwebsites.planclock.calendar.reminder;

import nl.starlightwebsites.planclock.Main;
import nl.starlightwebsites.planclock.models.Reminder;

import java.util.Random;
import java.util.TimerTask;

public class ReminderTask extends TimerTask {
    Reminder reminder;
    boolean tenMinuteMark = false;
    public ReminderTask(Reminder reminder){
        this.reminder = reminder;
    }
    public ReminderTask(Reminder reminder, boolean tenMinuteMark){
        this.reminder = reminder;
        this.tenMinuteMark = tenMinuteMark;
    }

    @Override
    public void run() {
        if(tenMinuteMark){
            Main.tts.say("This is a notification that you have the reminder '"+reminder.getTitle()+"' in 10 minutes");
        } else{
            if(reminder.getMotivations().size() == 0) {
                Main.tts.say("It is time to do the things for the reminder '" + reminder.getTitle() + "'");
            } else{
                int motivationToSay = reminder.getMotivations().size() * 2;
                motivationToSay = new Random().nextInt(motivationToSay);
                if(motivationToSay % 2 == 1){
                    Main.tts.say("It is time to do the things for the reminder '" + reminder.getTitle() + "'");
                } else{
                    Main.tts.say("It is time to do the things for the reminder '" + reminder.getTitle() + "' '"+ reminder.getMotivations().get(motivationToSay / 2) +"'");
                }
            }
        }
    }
}
