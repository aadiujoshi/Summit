package summit.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

//this class should be instantiated; not a singleton/static class
public class DBLogger extends FileOutputStream{

    private FileOutputStream log;
    
    public DBLogger getLogger(){
        try {
            return new DBLogger();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private DBLogger() throws FileNotFoundException{
        super("/server_logs/server_log" + Time.datetimeStamp('-'));
    }
    
    public void log(String msg){
        try {
            super.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
