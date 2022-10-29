package summit.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import summit.game.GameWorld;

public class GameLoader {
    
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

    public static boolean saveWorld(GameWorld world, String filename){
        try{
        FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);
              
            out.writeObject(world);
              
            out.close();
            file.close();

            //serialized properly
            return true;
        } catch(ClassCastException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
