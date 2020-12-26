package nl.starlightwebsites.planclock.calendar;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.EventReminder;
import nl.starlightwebsites.planclock.helpers.GoogleAuthenticate;

import java.util.List;

public class CalendarUtil {
    public static Calendar calendar;
    public static List<EventReminder> defaultReminders;
    public static void init() throws Exception{
         calendar = new Calendar.Builder(GoogleAuthenticate.httpTransport, GoogleAuthenticate.JSON_FACTORY, GoogleAuthenticate.credentials)
                .setApplicationName("Planclock and motivator")
                .build();

         // Get default reminders
        defaultReminders= calendar.calendarList().get("primary").execute().getDefaultReminders();
    }
}
