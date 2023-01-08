package summit.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import summit.game.GameWorld;

public class GameCrashReportGenerator {
    
    private GameCrashReportGenerator(){}

    public static void generateGameCrashReport(Exception cause, GameWorld world){

        cause = new Exception("llo");

        FileOutputStream cr = null;
        PrintWriter pw = null;

        String report = "";
        
        final String ln = "\n";

        try {
            //create file
            cr = new FileOutputStream("crash_reports/" + 
                world.getName() + 
                "_" + world.getSaveKey() + 
                "_" +Time.timestamp('.') + 
                ".txt");
                

            report += "Crash report at time: " + Time.timestamp(':') + ln;
            report += "GameWorld Name: " + world.getName() + ", Save Key: " + world.getSaveKey() + ln;
            report += "Seed: " + world.getSeed();
            
            report += ln+ln;

            //write exception
            StringWriter sw = new StringWriter();
            pw = new PrintWriter(sw);
            cause.printStackTrace(pw);
            report += sw.toString();

            report += ln+ln;

            report += world.toString();

            cr.write(report.getBytes());
                
        } catch (IOException e) {
            e.printStackTrace();
        } 
        finally{
            try {
                pw.close();
                cr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main0(String[] args) {
        // generateGameCrashReport(null, new GameWorld("hello", null, 0));
    }
}