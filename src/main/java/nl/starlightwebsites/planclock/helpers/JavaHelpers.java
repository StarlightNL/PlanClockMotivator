package nl.starlightwebsites.planclock.helpers;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class JavaHelpers {

    /**
     * This is a alternative to Thread.sleep with try catch included
     * @param millis Timeout in millis
     * @param threadName Custom thread name so that it can be easily interrupted
     * @return Returns true if sleep was interrupted
     */
    public static boolean sleep(long millis, String threadName){

        String oriThreadName = Thread.currentThread().getName();
        Thread.currentThread().setName(threadName); // Make sure that we can identify this thread
        boolean result = sleep(millis);
        Thread.currentThread().setName(oriThreadName); // Reset name to origional name
        return result;
    }
    /**
     * This is a alternative to Thread.sleep with try catch included
     * @param millis Timeout in millis
     * @return Returns true if sleep was interrupted
     */
    public static boolean sleep(long millis){
        try {
            Thread.sleep(millis);
            return false;
        } catch (InterruptedException ignored) {
            return true;
        }
    }

    public static int unsignedToByte(byte b) {
        return b & 0xFF;
    }
    public static byte[] intToBytes(int b) {
        return new byte[]{(byte)((b >> 8) & 0xFF),(byte)(b & 0xFF)};
    }


    public static void interruptThread(String threadName) {
        Set<Thread> threads = Thread.getAllStackTraces().keySet();
        threads.stream().filter(thread -> thread.getName().equalsIgnoreCase(threadName)).findFirst().ifPresent(thread -> thread.interrupt());
    }

    protected static void setEnv(Map<String, String> newenv) throws Exception {
        try {
            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);
            Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.putAll(newenv);
            Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            Map<String, String> cienv = (Map<String, String>)     theCaseInsensitiveEnvironmentField.get(null);
            cienv.putAll(newenv);
        } catch (NoSuchFieldException e) {
            Class[] classes = Collections.class.getDeclaredClasses();
            Map<String, String> env = System.getenv();
            for(Class cl : classes) {
                if("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                    Field field = cl.getDeclaredField("m");
                    field.setAccessible(true);
                    Object obj = field.get(env);
                    Map<String, String> map = (Map<String, String>) obj;
                    map.clear();
                    map.putAll(newenv);
                }
            }
        }
    }
}
