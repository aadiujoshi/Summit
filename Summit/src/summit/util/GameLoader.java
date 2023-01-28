
package summit.util;
/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.HashMap;

import summit.game.GameWorld;

/**
 * This class is responsible for GameWorld seriliazation/deserialization (game saves). 
 * Serliazation data is sent and retrieved from the database, which is accessed through the
 * {@link DBConnection} class. 
 */
public class GameLoader {
    
    private GameLoader(){}

    private static volatile boolean accessing;

    public static final String tempFile = "cache/temp.txt";

    public static SystemLogger logger = SystemLogger.getLogger();

    /**
     * Creates a new entry in the database using {@code DBConnection}.
     * 
     * 
     * @param saveKey The {@code SAVE_KEY} of the GameWorld 
     * 
     * @see DBConnection#createSave(String)
     * @see GameWorld#getSaveKey()
     */
    public static void createSave(String saveKey, String saveName){
        DBConnection.createSave(saveKey, saveName);
    }

    
    /** 
     * @param world
     */
    private static void saveProtocol(GameWorld world){
        try{
            FileOutputStream file = new FileOutputStream(tempFile);
            ObjectOutputStream out = new ObjectOutputStream(file);
            
            out.writeObject(world);
            
            out.close();
            file.close();
        } catch (IOException e){
            e.printStackTrace(logger);
        }
    }


    /**
     * Retrieves all the saved {@code GameWorld} keys and names, and returns a mapped
     * {@code HashMap<String, String>}
     *
     * @return A {@code HashMap<String, String>} containing a list of the saved GameWorld
     *          {@code SaveName} (Key) and {@code SaveKey} (Value)
     */
    public static HashMap<String, String> getSaves(){
        return DBConnection.getSaves();
    }

    /**
     * Returns a deserialized GameWorld object. Serlialized data is retrieved 
     * from the database
     * 
     * @param saveKey The {@code SAVE_KEY} of the GameWorld 
     * @return The GameWorld object associated with {@code saveKey} 
     * 
     * @see GameWorld#getSaveKey()
     * @see DBConnection#getSave(String)
     */
    public static GameWorld loadWorld(String saveKey){
        Time.waitWhile((Object obj) -> {
            return accessing;
        });

        try{
            File dbOut = DBConnection.getSave(saveKey);

            if(dbOut == null)
                return null;

            FileInputStream file = new FileInputStream(dbOut);
            ObjectInputStream out = new ObjectInputStream(file);
                
            GameWorld world = (GameWorld)out.readObject();
            
            out.close();
            file.close();
            
            return world;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace(logger);
        }

        //return null if error
        return null;
    }
    
    /**
     * Serializes {@code world} and outputs it to {@code cache/temp.txt}, then updates the database
     * with {@code DBConnection}
     * 
     * However, if {@code GameWorld.getCompletion() == GameWorld.GAME_OVER_PLAYER_DEAD} is true, 
     * the database entry associated with {@code saveKey} will be deleted, and {@code world} will
     * not be saved.
     * 
     * @param world The GameWorld to be saved
     */
    public static void saveWorld(GameWorld world){

        if(world.getCompletion() == GameWorld.GAME_OVER_PLAYER_DEAD){
            logger.log("Will not save world with completion status: GameWorld.GAME_OVER_PLAYER_DEAD");
            DBConnection.removeSave(world.getSaveKey());
            try {
                Files.newBufferedWriter(
                    FileSystems.getDefault().getPath(tempFile), 
                    java.nio.file.StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace(logger);
            }
            return;
        }
        
        Time.waitWhile((Object obj) -> {
            return accessing;
        });

        accessing = true;

        try{
            saveProtocol(world);

            logger.log("Save to temp file complete");
            DBConnection.updateSave(world.getSaveKey(), world.getElapsedTime(), world.getCompletion());

            accessing = false;
            
        } catch(ClassCastException e) {
            e.printStackTrace(logger);
        }
    }

    
    /** 
     * @return GameWorld
     */
    public static GameWorld loadCache(){
        Time.waitWhile((Object obj) -> {
            return accessing;
        });

        accessing = true;

        FileInputStream file = null;
        ObjectInputStream in = null;

        try{
            file = new FileInputStream(tempFile);
            byte[] bytes = file.readAllBytes();
            in = new ObjectInputStream(new ByteArrayInputStream(bytes));
                
            //empty cache
            if(bytes.length == 0){
                logger.log("Nothing in the cache file... Either no worlds have been loaded or saved, or you died on the previously loaded world");
                return null;
            }

            GameWorld world = (GameWorld)in.readObject();
            
            return world;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace(logger);
        } finally {
            accessing = false;
            try {
                file.close();
                in.close();
            } catch (NullPointerException e) {
            } catch (IOException e) {
                e.printStackTrace(logger);
            }
        }

        //return null if error
        return null;
    }

    
    /** 
     * @param world
     */
    public static void asyncSaveWorld(GameWorld world){
        
        Time.waitWhile((Object obj) -> {
            return accessing;
        });

        accessing = true;

        Thread wr = new Thread(() -> {
            try{
                saveProtocol(world);
    
                logger.log("Save to temp file complete");
                DBConnection.updateSave(world.getSaveKey(), world.getElapsedTime(), world.getCompletion());

                accessing = false;

            } catch(ClassCastException e) {
                e.printStackTrace(logger);
            }
        }, "upscaler");
        wr.start();

        Thread updater = new Thread(() -> {
            while(wr.isAlive()){
                logger.log("Saving world...");
                Time.nanoDelay(Time.NS_IN_S);
            }
            logger.log("Save complete");
            accessing = false;
        });

        updater.start();
    }
}
