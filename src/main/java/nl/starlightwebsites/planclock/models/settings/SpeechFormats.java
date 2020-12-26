package nl.starlightwebsites.planclock.models.settings;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class SpeechFormats {
    @JsonProperty("reminderpre")
    public String reminderPreText = "In %d minute%s you have the reminder %s";
    @JsonProperty("reminderdue")
    public String reminderDueText = "In %d minute%s you have the reminder %s";
}
