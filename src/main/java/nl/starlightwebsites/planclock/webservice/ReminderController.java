package nl.starlightwebsites.planclock.webservice;

import nl.starlightwebsites.planclock.calendar.reminder.ReminderClient;
import nl.starlightwebsites.planclock.calendar.reminder.ReminderTask;
import nl.starlightwebsites.planclock.database.DBReminder;
import nl.starlightwebsites.planclock.models.Reminder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/reminders")
public class ReminderController {

    @RequestMapping(value = "/list", method = GET)
    public List<Reminder> List() {
        return new ArrayList<>(ReminderClient.reminders.values());
    }

    @RequestMapping(value = "/get/{id}", method = GET)
    public Reminder Get(@PathVariable int id) {
        return ReminderClient.reminders.get(id);
    }

    @RequestMapping(value = "/play/{dbid}", method = GET)
    public String Play(@PathVariable int id) {
        new ReminderTask(ReminderClient.getReminders().stream().filter(r -> r.getDbid() == id).findAny().get()).run();
        return "";
    }
//
//    @RequestMapping(value = "/create", method = POST)
//    public Reminder Create(@RequestBody Reminder motivation) {
//        return DBReminder.addReminder(motivation.getReminderId(), motivation.getReminderText());
//    }

//    @RequestMapping(value = "/delete/{id}", method = GET)
//    public void Delete(@PathVariable int id) {
//        DBReminder.deleteReminderById(id);
//    }

//    @RequestMapping(value = "/update/{id}", method = POST)
//    public Reminder Update(@PathVariable int id, @RequestBody Reminder motivation) {
//        return DBReminder.updateReminder(id, motivation);
//    }
}
