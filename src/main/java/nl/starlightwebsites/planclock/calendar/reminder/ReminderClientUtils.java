package nl.starlightwebsites.planclock.calendar.reminder;

import lombok.SneakyThrows;
import nl.starlightwebsites.planclock.Main;
import org.tinylog.Logger;

import javax.annotation.Nullable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

public class ReminderClientUtils {
    @SneakyThrows
    public static String createListRequestBody(int maxReminders, @Nullable Instant maxTimeOfReminders){
        HashMap<String, Object> body = new HashMap<>();
        body.put("5", 0); // Boolean field. What value to use is unknown. 0 seems to work
        body.put("6", maxReminders);
        if(maxTimeOfReminders != null){
            if(maxTimeOfReminders.isBefore(Instant.now())){
                Logger.debug("MaxTimeOfReminders is lower then now. Is this wanted?");
            }
            maxTimeOfReminders = maxTimeOfReminders.plus(1, ChronoUnit.DAYS); // Because of a bug we need to add some time. We just do one day because that is nice
            body.put("16",maxTimeOfReminders.toEpochMilli()); // This needs to be milliseconds
        }
        return Main.json.writeValueAsString(body);
    }
}
