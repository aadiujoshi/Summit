/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import summit.game.GameWorld;

/**
 * This class is responsible for GameWorld seriliazation/deserialization (game saves). 
 * Serliazation data is sent and retrieved from the database, which is accessed through the
 * {@code DBConnection} class. 
 */
public class GameLoader {
    
    private GameLoader(){}

    private static volatile boolean accessing;

    public static final String tempFile = "gamesaves/temp.txt";

    /**
     * Returns a deserialized GameWorld object. Serlialized data is retrieved 
     * from the database
     * 
     * @param saveName The name of the GameWorld 
     * @return The GameWorld object associated with {@code saveName} 
     * 
     * @see GameWorld#getSaveName()
     * @see DBConnection#getDBSave(String)
     */
    public synchronized static GameWorld loadWorld(String saveName){
        while(accessing){ 
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } 
        }

        try{
            FileInputStream file = new FileInputStream(tempFile);
            ObjectInputStream out = new ObjectInputStream(file);
                
            GameWorld world = (GameWorld)out.readObject();
            
            out.close();
            file.close();
            
            return world;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        //return null if error
        return null;
    }
    
    /**
     * Saves a GameWorld to the database
     * 
     * @param world The GameWorld to be saved
     */
    public synchronized static void saveWorld(GameWorld world){
        while(accessing){ 
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } 
        }

        accessing = true;

        try{
            FileOutputStream file = new FileOutputStream(tempFile);
            ObjectOutputStream out = new ObjectOutputStream(file);
            
            out.writeObject(world);
            
            out.close();
            file.close();

            accessing = false;
            System.out.println("Save complete");
            
        } catch(ClassCastException | IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void asyncSaveWorld(GameWorld world){
        while(accessing){ 
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } 
        }

        accessing = true;

        Thread wr = new Thread(() -> {
            try{
                FileOutputStream file = new FileOutputStream(tempFile);
                ObjectOutputStream out = new ObjectOutputStream(file);
                
                out.writeObject(world);
                
                out.close();
                file.close();
    
                accessing = false;
                System.out.println("Save complete");
                
            } catch(ClassCastException | IOException e) {
                e.printStackTrace();
            }
        });
        wr.start();

        Thread updater = new Thread(() -> {
            while(wr.isAlive()){
                System.out.println("Saving world...");
                Time.nanoDelay(Time.NS_IN_S);
            }
            System.out.println("Save complete");
            accessing = false;
        });

        updater.start();
    }
}
