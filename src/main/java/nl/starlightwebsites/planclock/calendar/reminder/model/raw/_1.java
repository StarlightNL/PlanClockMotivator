
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
    "title",
    "date",
    "13",
    "18",
    "21",
    "24",
    "26",
    "repeatSchedule",
    "22",
    "23"
})
public class _1 {

    @JsonProperty("1")
    public _1_ _1;
    @JsonProperty("2")
    public nl.starlightwebsites.planclock.calendar.reminder.model.raw._2 _2;
    @JsonProperty("3")
    public String title;
    @JsonProperty("5")
    public nl.starlightwebsites.planclock.calendar.reminder.model.raw._5 date;
    @JsonProperty("13")
    public nl.starlightwebsites.planclock.calendar.reminder.model.raw._13 _13;
    @JsonProperty("18")
    public String _18;
    @JsonProperty("21")
    public Integer _21;
    @JsonProperty("24")
    public Integer _24;
    @JsonProperty("26")
    public String _26;
    @JsonProperty("16")
    public nl.starlightwebsites.planclock.calendar.reminder.model.raw._16 repeatSchedule;
    @JsonProperty("22")
    public Integer _22;
    @JsonProperty("23")
    public String _23;
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
