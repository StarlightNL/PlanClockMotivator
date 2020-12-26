package nl.starlightwebsites.planclock.models;

import lombok.Getter;
import lombok.Setter;
import nl.starlightwebsites.planclock.helpers.BitHelpers;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class TimePlanning {
    @Getter @Setter private int id = -1;
    @Getter @Setter private int dayBitmask = 0;
    @Getter @Setter private Time plannedTime = new Time(0);
    @Getter @Setter private int weeksBetween = 0;
    public TimePlanning(){}

    public void setPlannedTimeWithInstant(Instant datetime){
        plannedTime = new Time(datetime.toEpochMilli());
    }

    public void setDayBitmaskWithDayNumbers(List<Integer> dayNumbers){
        for(int dayNumber : dayNumbers){
            if(dayNumber < 8){
                dayBitmask = BitHelpers.bitSet(dayBitmask,dayNumber - 1);
            }
        }
    }

    public boolean dayIsSet(DayOfWeek day){
        return BitHelpers.bitRead(dayBitmask, day.getValue() - 1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("id: %d ",id));
        sb.append("Time: ");
        sb.append(plannedTime);
        sb.append(" Days: ");
        if(dayBitmask != 0) {
            for (int i = 0; i < 7; i++) {
                if ((dayBitmask & (1 << i)) != 0) {
                    sb.append(DayOfWeek.of(i + 1));
                    sb.append(", ");
                }
            }
        } else{
            sb.append("none ");
        }
        sb.append("Weeks between: ");
        sb.append(weeksBetween);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimePlanning that = (TimePlanning) o;
        return  dayBitmask == that.dayBitmask &&
                weeksBetween == that.weeksBetween &&
                plannedTime.equals(that.plannedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayBitmask, plannedTime, weeksBetween);
    }
}
