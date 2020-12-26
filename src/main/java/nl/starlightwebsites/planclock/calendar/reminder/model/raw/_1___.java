
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
    "8",
    "9"
})
public class _1___ {

    @JsonProperty("1")
    public Integer _1;
    @JsonProperty("2")
    public Integer _2;
    @JsonProperty("3")
    public Integer _3;
    @JsonProperty("4")
    public _4_ _4;
    @JsonProperty("8")
    public Integer _8;
    @JsonProperty("9")
    public Integer _9;
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
