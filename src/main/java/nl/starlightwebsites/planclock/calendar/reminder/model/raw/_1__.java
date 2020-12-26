
package nl.starlightwebsites.planclock.calendar.reminder.model.raw;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "1",
    "2",
    "3",
    "4",
    "5",
    "6"
})
public class _1__ {

    @JsonProperty("1")
    public Integer repeatIntervalType; // If 0 then type is day. If 1 then type is week
    @JsonProperty("2")
    public Integer repeatInterval;
    @JsonProperty("3")
    public nl.starlightwebsites.planclock.calendar.reminder.model.raw._3 _3;
    @JsonProperty("4")
    public _4__ _4;
    @JsonProperty("5")
    public _5_ _5;
    @JsonProperty("6")
    public nl.starlightwebsites.planclock.calendar.reminder.model.raw._6 daysOfTheWeek;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
