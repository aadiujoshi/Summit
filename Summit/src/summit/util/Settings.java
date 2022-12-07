package summit.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import summit.Main;

public class Settings {

    private static FileWriter file;
    private static HashMap<String, String> properties;

    static{
        try {
            properties = new HashMap<>();

            Scanner s = new Scanner(new File(Main.path + "settings.txt"));

            while(s.hasNextLine()) {
                properties.put(s.next(), s.next());
            }

            System.out.println(properties);

            file = new FileWriter(new File(Main.path + "settings.txt"));

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

        System.out.println(newSettings);

        try{
            file.write(newSettings);
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

        //parse
        if(p.equals("true"))
            return true;
        
        if(p.equals("false"))
            return false;

        if(((int)p.charAt(0))-48 >= 0 && ((int)p.charAt(0))-48 <= 9)
            return ((int)p.charAt(0))-48;
        
        //return as boring-ass string
        return p;
    }
}
