package nl.starlightwebsites.planclock.helpers;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import nl.starlightwebsites.planclock.Main;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.tinylog.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.http.HttpClient;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.cert.CRLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class GoogleAuthenticate {
    public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    public static NetHttpTransport  httpTransport;
    static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".planclock/planclock_cred");
    public static FileDataStoreFactory dataStoreFactory;
    public static Credential credentials;
    public static void init() throws GeneralSecurityException, IOException {
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        // initialize the data store factory
        dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
    }

    public static Credential authorize() throws Exception {
        //Export service credentials to filesystem
        try {
            InputStreamReader isr = new InputStreamReader(Main.class.getResourceAsStream("/service_secrets.json"));
            FileWriter fw = new FileWriter(Paths.get(System.getProperty("user.home"), ".planclock/service_secrets.json").toString());
            while (isr.ready()) {
                fw.write(isr.read());
            }
            fw.flush();
            fw.close();
            isr.close();
            HashMap<String, String> newEnv = new HashMap<>();
            newEnv.put("GOOGLE_APPLICATION_CREDENTIALS",Paths.get(System.getProperty("user.home"), ".planclock/service_secrets.json").toString());
            JavaHelpers.setEnv(newEnv);
        } catch(Exception ex){
            Logger.error(ex,"Failed to export service_secrets.json to filesystem");
            return null;
        }
        // load client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(Main.class.getResourceAsStream("/client_secrets.json")));
        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets,
                Arrays.asList(CalendarScopes.CALENDAR, "https://www.googleapis.com/auth/reminders")).setDataStoreFactory(dataStoreFactory)
                .build();
        // authorize
        credentials = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        return credentials;
    }
}
