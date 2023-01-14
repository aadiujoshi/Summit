package summit.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

//this class should be instantiated; not a singleton/static class
public class SystemLogger extends PrintStream{
    
    private SystemLogger(String fileName) throws FileNotFoundException {
        super(fileName);
        // super.
    }

    
    /** 
     * @return SystemLogger
     */
    public static SystemLogger getLogger(){
        try {
            final String path = "server_logs/server_log_" + Time.datetimeStamp('-') + ".txt";
            System.out.println(path);
            new File(path).createNewFile();
            return new SystemLogger(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void log(){
        // super.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        super.println();
    }

    
    /** 
     * @param msg
     */
    public void log(String msg){
        super.println(Time.timestamp(':') + " " + msg);
    }
}
