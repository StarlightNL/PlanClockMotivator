package nl.starlightwebsites.planclock.models;

import lombok.Setter;
import lombok.Getter;
import nl.starlightwebsites.planclock.models.database.Motivation;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Reminder implements Comparable<Reminder>{
   @Getter @Setter private String id = "";
   @Getter @Setter private int dbid = 0;
   @Getter @Setter private String title = "";
   @Getter @Setter private Instant plannedTime = Instant.now();
   @Getter @Setter private boolean done = false;
   @Getter @Setter private boolean googleReminder = true;
   @Getter private List<Motivation> motivations = new ArrayList<>();
   @Getter private List<TimePlanning> timePlannings = new ArrayList<>();

    public Reminder(){};

    private int day;

    public Reminder(String googleid, String title, int year, int month, int day, int hour, int minute, int second) {
       this.id = googleid;
       this.title = title;
       this.plannedTime = ZonedDateTime.of(year, month, day, hour, minute, second,0, ZoneId.systemDefault()).toInstant();
    }


    @Override
    public int compareTo(Reminder r) {
        if(getPlannedTime() == null || r.getPlannedTime() == null) return 0;
        return getPlannedTime().compareTo(r.getPlannedTime());
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.systemDefault());
        return String.format("DBID %d. %s %s at %s",dbid, done ? "Did" : "Do", title, formatter.format(plannedTime));
    }

    public void addMotivations(List<Motivation> motivations){
        this.motivations.addAll(motivations);
    }

    public void addMotivation(Motivation motivation){
        motivations.add(motivation);
    }

    public void addTimePlanning(TimePlanning tp){
        timePlannings.add(tp);
    }

    public void addTimePlannings(List<TimePlanning> timePlanningList){
        timePlannings.addAll(timePlanningList);
    }

    public boolean isBeforeNow() {
        return getPlannedTime().isBefore(Instant.now());
    }

    public String getGoogleId() {
        return this.id;
    }

    public int getDbid() {
        return this.dbid;
    }

    public String getTitle() {
        return this.title;
    }

    public Instant getPlannedTime() {
        return this.plannedTime;
    }

    public boolean isDone() {
        return this.done;
    }

    public List<Motivation> getMotivations() {
        return this.motivations;
    }

    public List<TimePlanning> getTimePlannings() {
        return this.timePlannings;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDbid(int dbid) {
        this.dbid = dbid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPlannedTime(Instant plannedTime) {
        this.plannedTime = plannedTime;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
