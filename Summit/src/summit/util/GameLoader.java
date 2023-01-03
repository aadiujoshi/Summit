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
 * {@link DBConnection} class. 
 */
public class GameLoader {
    
    private GameLoader(){}

    private static volatile boolean accessing;

    public static final String tempFile = "gamesaves/temp.txt";

    /**
     * Creates a new entry in the database using {@code DBConnection}.
     * 
     * 
     * @param saveName The name of the GameWorld 
     * 
     * @see DBConnection#createSave(String)
     * @see GameWorld#getSaveName()
     */
    public synchronized static void createSave(String saveName){
        DBConnection.createSave(saveName);
    }

    /**
     * Returns a deserialized GameWorld object. Serlialized data is retrieved 
     * from the database
     * 
     * @param saveName The name of the GameWorld 
     * @return The GameWorld object associated with {@code saveName} 
     * 
     * @see GameWorld#getSaveName()
     * @see DBConnection#getSave(String)
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
            File dbOut = DBConnection.getSave(saveName);

            FileInputStream file = new FileInputStream(dbOut);
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
     * Serializes {@code world} and outputs it to {@code gamesaves/temp.txt}, then updates the database
     * with {@code DBConnection}
     * 
     * However, if {@code GameWorld.getCompletion() == GameWorld.GAME_OVER_PLAYER_DEAD} is true, 
     * the database entry associated with {@code saveName} will be deleted, and {@code world} will
     * not be saved.
     * 
     * @param world The GameWorld to be saved
     */
    public synchronized static void saveWorld(GameWorld world){

        if(world.getCompletion() == GameWorld.GAME_OVER_PLAYER_DEAD){
            System.out.println("Will not save world with completion status: GameWorld.GAME_OVER_PLAYER_DEAD");
            DBConnection.removeSave(world.getSaveName());
            return;
        }
        
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

            System.out.println("Save to temp file complete");
            DBConnection.updateSave(world.getSaveName(), world.getElapsedTime(), world.getCompletion());

            accessing = false;
            
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
    
                System.out.println("Save to temp file complete");
                DBConnection.updateSave(world.getSaveName(), world.getElapsedTime(), world.getCompletion());

                accessing = false;

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
