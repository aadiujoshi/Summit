/*
* BPA project by Aadi Joshi, Aditya Bhattaharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import summit.game.GameWorld;

public class GameLoader {
    
    private GameLoader(){}

    public static GameWorld loadWorld(String filename){
        try{
            FileInputStream file = new FileInputStream(filename);
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

    public static void saveWorld(GameWorld world, String filename){
        Runnable r = () -> {
            try{
                FileOutputStream file = new FileOutputStream(filename);
                ObjectOutputStream out = new ObjectOutputStream(file);
                
                out.writeObject(world);
                
                out.close();
                file.close();
            } catch(ClassCastException | IOException e) {
                e.printStackTrace();
            }
        };
        Thread wr = new Thread(r);
        wr.start();
    }
}
