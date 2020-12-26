package nl.starlightwebsites.planclock.models.database;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public class Motivation {
    @Getter @Setter private int id;
    @Getter @Setter private int reminderId;
    @Getter @Setter private String motivationText;
    @JsonProperty("createdate")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    @Getter @Setter private Instant createDate;
    @JsonProperty("updatedate")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    @Getter @Setter private Instant updateDate;
    @Getter @Setter private String reminderText;

    public Motivation(){}

    public Motivation(int id, int reminderId, String motivationText, Instant createDate, Instant updateDate, String reminderText) {
        this.id = id;
        this.reminderId = reminderId;
        this.motivationText = motivationText;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.reminderText = reminderText;
    }
}
