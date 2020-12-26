package nl.starlightwebsites.planclock.helpers;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeHelpers {
    public static Instant timeFromYMDHMS(int year, int month, int day, int hour, int minute, int second){
        return ZonedDateTime.of(year, month, day, hour, minute, second,0, ZoneId.systemDefault()).toInstant();
    }
}
