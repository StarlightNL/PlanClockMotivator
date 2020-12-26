package nl.starlightwebsites.planclock.helpers;

import org.apache.commons.exec.*;
import org.apache.commons.lang3.SystemUtils;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExecHelper {

    public static void killViaPID(String pid){
        if(SystemUtils.IS_OS_WINDOWS){
            ExecHelper.StartProgram("taskkill ", "/PID " + pid, true, 1000);
        } else {
            ExecHelper.StartProgram("kill ", pid, true, 1000);
        }
    }

    public static List<List<String>> StartProgramErrOut(String program, String params, Boolean blocking){
        return StartProgramErrOut(program, params, blocking, 0);
    }

    public static List<List<String>> StartProgramErrOut(String program, String params, Boolean blocking, Integer sleepTime) {
        if(SystemUtils.IS_OS_WINDOWS){
            if(program.startsWith("sudo ")) program = program.replace("sudo ","");
        }
        List<List<String>> result = new ArrayList<>();
        try {
            CollectingLogOutputStream stdout = new CollectingLogOutputStream();
            CollectingLogOutputStream stderr = new CollectingLogOutputStream();
            PumpStreamHandler ps = new PumpStreamHandler(stdout, stderr);
            DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
            CommandLine cl = CommandLine.parse(program + " " + params + "2>&1" +(blocking ? "" : " &"));
            Executor executor = new DefaultExecutor();
            executor.setStreamHandler(ps);
            executor.execute(cl, resultHandler);
            if(sleepTime != null || sleepTime > 0){
                Thread.sleep(sleepTime);
            }
            result.add(stdout.getLines());
            result.add(stderr.getLines());
            return result;
        } catch (ExecuteException e) {
            Logger.warn(e);
        } catch (IOException e) {
            Logger.warn(e);
        } catch (InterruptedException e) {
            Logger.debug("Could not sleep\r\n{}",e.getMessage());
        }
        return new ArrayList<>();
    }

    public static List<String> StartProgram(String program, String params, Boolean blocking, Boolean inControlGroup){
        return StartProgram(program, params, blocking, inControlGroup,  0,blocking);
    }

    public static List<String> StartProgram(String program, String params, Boolean blocking, int sleepTime){
        if(SystemUtils.IS_OS_WINDOWS){
            if(program.startsWith("sudo ")) program = program.replace("sudo ","");
        }
        return StartProgram(program, params, blocking, true,  sleepTime,blocking);
    }

    public static List<String> StartProgram(String program, String params, Boolean blocking){
        return StartProgram(program, params, blocking, true,  0,blocking);
    }

    public static List<String> StartProgram(String program, String params, Boolean blocking, Boolean inControlGroup, Integer sleepTime, Boolean waitFor) {
//        if(program.contains("\\|") || params.contains("\\|")){ // For pipe support?
//            program = "bash -c " + program;
//        }
        if(SystemUtils.IS_OS_WINDOWS){
            if(program.startsWith("sudo ")) program = program.replace("sudo ","");
        }
        try {
            CollectingLogOutputStream stdout = new CollectingLogOutputStream();
            PumpStreamHandler ps = new PumpStreamHandler(stdout);
            DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
            CommandLine cl = CommandLine.parse((inControlGroup ? "" : "systemd-run --scope ") + program + " " + params + (blocking ? "" : " &"));
            //Logger.trace(cl.toString());
            Executor executor = new DefaultExecutor();
            executor.setStreamHandler(ps);
            executor.execute(cl, resultHandler);
            if(waitFor){
                resultHandler.waitFor();
            } else {
                if (sleepTime != null || sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            }
            return stdout.getLines();
        } catch (ExecuteException e) {
            Logger.warn("ExecuteException");
            Logger.warn(e);
        } catch (IOException e) {
            Logger.warn(e);
        } catch (InterruptedException e) {
            Logger.debug("Could not sleep\r\n{}",e.getMessage());
            Logger.warn(e);
        }
        return new ArrayList<>();
    }
}
