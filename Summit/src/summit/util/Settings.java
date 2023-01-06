package summit.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {

    // private static HashMap<String, String> properties = new HashMap<>();
    private static Properties properties;

    static{
        FileOutputStream fos = null; 

        properties = DBConnection.getSettings();
        
        // System.out.println(properties);

        try {
            fos = new FileOutputStream("settings.properties");
            properties.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        } 
        finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }

    private static void updateFile(){
        FileOutputStream fos = null;
        
        try{
            fos = new FileOutputStream("settings.properties");
            properties.store(fos, "");
            
        } catch(IOException e){
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void changeSetting(String key, Object newVal){
        properties.put(key, newVal);
        updateFile();
    }

    public static Object getSetting(String key){
        String p = (String)properties.get(key);

        if(p == null)
            throw new Error("No such setting: " + key);

        //parse
        if(p.equals("true"))
            return true;
        
        if(p.equals("false"))
            return false;

        if(((int)p.charAt(0))-48 >= 0 && ((int)p.charAt(0))-48 <= 9)
            return ((int)p.charAt(0))-48;
        
        //return as string
        return p;
    }
}
