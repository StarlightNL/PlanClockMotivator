package nl.starlightwebsites.planclock.webservice;

import nl.starlightwebsites.planclock.Main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class Webservice {
    public static void init(){
        SpringApplication app = new SpringApplication(Webservice.class);
        app.setDefaultProperties(Collections
                .singletonMap("server.port", "8083"));
        app.run("");
    }
}
