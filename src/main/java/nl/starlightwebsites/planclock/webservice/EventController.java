package nl.starlightwebsites.planclock.webservice;

import com.google.api.services.calendar.model.Event;
import nl.starlightwebsites.planclock.calendar.event.EventClient;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/event")
public class EventController {

//    @RequestMapping(value = "/get/{id}", method = GET)
//    public Event Get(@PathVariable(name = "id", value = "0") int id) {
//        return EventClient.getEvents(id);
//    }
    @RequestMapping(value = "/list", method = GET)
    public ConcurrentHashMap<String, Event> List() {
        return EventClient.events;
    }
}