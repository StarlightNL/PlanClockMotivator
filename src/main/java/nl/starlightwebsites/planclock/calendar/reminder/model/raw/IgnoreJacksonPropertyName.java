package nl.starlightwebsites.planclock.calendar.reminder.model.raw;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

import java.lang.annotation.Annotation;

public class IgnoreJacksonPropertyName extends JacksonAnnotationIntrospector {
    @Override
    protected <A extends Annotation> A _findAnnotation(Annotated annotated, Class<A> annoClass) {
        if (annoClass == JsonProperty.class) {
            return null;
        }
        if (annoClass == JsonPropertyOrder.class) {
            return null;
        }
        return super._findAnnotation(annotated, annoClass);
    }
}