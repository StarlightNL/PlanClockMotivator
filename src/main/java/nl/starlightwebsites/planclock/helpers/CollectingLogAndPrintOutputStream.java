package nl.starlightwebsites.planclock.helpers;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.tinylog.Logger;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class CollectingLogAndPrintOutputStream implements ExecuteStreamHandler {
    private final List<String> lines = new LinkedList<String>();
    public void setProcessInputStream(OutputStream outputStream) throws IOException
    {
    }
    //important - read all output line by line to track errors
    public void setProcessErrorStream(InputStream inputStream) throws IOException {
        new Thread(()-> {
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            try {
                while ((line = br.readLine()) != null) {
                    //use lines whereever you want - for now just print on console
                    System.out.println("error: " + line);
                }
            } catch(Exception ex){
                Logger.warn(ex);
            }
        }, "ProcessErrorReader").start();
    }
    //important - read all output line by line to track process output
    public void setProcessOutputStream(InputStream inputStream) throws IOException
    {
        new Thread(()-> {
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);
            String line = "";

            try {
                while ((line = br.readLine()) != null) {
                    //use lines whereever you want - for now just print on console
                    System.out.println("output: " + line);
                }
            } catch(Exception ex){
                Logger.warn(ex);
            }
        }, "ProcessOutputReader").start();
    }

    public void start() throws IOException {
    }

    public void stop() throws IOException {
    }
}