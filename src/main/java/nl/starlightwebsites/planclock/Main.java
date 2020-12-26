package nl.starlightwebsites.planclock;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.starlightwebsites.planclock.calendar.CalendarUtil;
import nl.starlightwebsites.planclock.calendar.event.EventClient;
import nl.starlightwebsites.planclock.calendar.reminder.ReminderClient;
import nl.starlightwebsites.planclock.calendar.reminder.model.raw.IgnoreJacksonPropertyName;
import nl.starlightwebsites.planclock.database.DBConnector;
import nl.starlightwebsites.planclock.database.DBReminder;
import nl.starlightwebsites.planclock.helpers.GoogleAuthenticate;
import nl.starlightwebsites.planclock.helpers.JavaHelpers;
import nl.starlightwebsites.planclock.tts.AudioOutputThread;
import nl.starlightwebsites.planclock.tts.GoogleTTS;
import nl.starlightwebsites.planclock.tts.interfaces.iTTSExec;
import nl.starlightwebsites.planclock.webservice.Webservice;
import org.springframework.boot.SpringApplication;
import org.tinylog.Logger;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

public class Main {
    /**
     * TTS that is for now Google
     */
    public static iTTSExec tts = new GoogleTTS();
//    public static Gson gson = new Gson();
    public static ObjectMapper jsonIgnoreAn = new ObjectMapper().setAnnotationIntrospector(new IgnoreJacksonPropertyName());
    public static ObjectMapper json = new ObjectMapper();
    public static Timer globalTimer = new Timer();
    /**
     * Timer specific for calender events
     */
    public static Timer calenderTimer = new Timer();
    public static void main(String[] args){
        initDependency(GoogleAuthenticate.class,"init");
        initDependency(GoogleAuthenticate.class,"authorize");
        initDependency(DBConnector.class,"connectToDatabase");
        initDependency(CalendarUtil.class,"init");
        initDependency(AudioOutputThread.class,"startAudioPlayer");
        initDependency(Webservice.class,"init");
        if(args.length > 0){
            List<String> argsList = Arrays.asList(args);
            if(argsList.contains("drop")) {
                DBConnector.dropDataBase();
            }
        }

        tts.init();
        DBReminder.getAllReminders();
        EventClient.getEvents();
        ReminderClient.startPollingReminders();
        EventClient.startPollingEvents();

        JavaHelpers.sleep(500);
        while(true){
            JavaHelpers.sleep(1000);
        }
    }


    public static void initDependency(Class clazz, String methodName){
        try {
            Method method = clazz.getMethod(methodName);
            method.invoke(method.getClass());
        } catch(Exception ex){
            Logger.error(ex,"Failed to call function {} in class {}", methodName, clazz.getName());
            System.exit(1);
        }
    }
}
