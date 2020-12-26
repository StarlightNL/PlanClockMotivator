package nl.starlightwebsites.planclock.webservice;

import nl.starlightwebsites.planclock.calendar.reminder.ReminderClient;
import nl.starlightwebsites.planclock.database.DBMotivation;
import nl.starlightwebsites.planclock.models.Reminder;
import nl.starlightwebsites.planclock.models.database.Motivation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/motivations")
public class MotivationsControlleer {
    @RequestMapping(value = "/list", method = GET)
    public List<Motivation> List() {
        return DBMotivation.getAllMotivations();
    }

    @RequestMapping(value = "/get/{id}", method = GET)
    public Motivation Get(@PathVariable int id) {
        return DBMotivation.getMotivationById(id);
    }

    @RequestMapping(value = "/create", method = POST)
    public Motivation Create(@RequestBody Motivation motivation) {
        return DBMotivation.addMotivation(motivation.getReminderId(), motivation.getMotivationText());
    }

    @RequestMapping(value = "/delete/{id}", method = GET)
    public void Delete(@PathVariable int id) {
        DBMotivation.deleteMotivationById(id);
    }

    @RequestMapping(value = "/update/{id}", method = POST)
    public Motivation Update(@PathVariable int id, @RequestBody Motivation motivation) {
        return DBMotivation.updateMotivation(id, motivation);
    }
}
