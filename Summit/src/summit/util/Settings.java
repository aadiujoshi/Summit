package summit.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import summit.Main;

public class Settings {

    private static HashMap<String, String> properties = new HashMap<>();

    static{
        try {
            Scanner s = new Scanner(new File(Main.path + "settings.txt"));

            while(s.hasNext()) {
                properties.put(s.next(), s.next());
            }

            System.out.println("Settings: " + properties);

            updateFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updateFile(){
        String newSettings = "";

        for (Map.Entry<String, String> setting : properties.entrySet()) {
            newSettings += setting.getKey() + " " + setting.getValue() + "\n";
        }
        
        try{
            FileWriter file = new FileWriter(new File(Main.path + "settings.txt"));
            file.write(newSettings);
            file.flush();
            file.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void changeSetting(String key, Object newVal){
        properties.put(key, newVal+"");
        updateFile();
    }

    public static Object getSetting(String key){
        String p = properties.get(key);

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
