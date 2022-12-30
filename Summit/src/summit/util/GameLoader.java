/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
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

    private static volatile boolean accessing;

    public static GameWorld loadWorld(String filename){
        while(accessing){ 
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } 
        }

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
        while(accessing){ 
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } 
        }

        accessing = true;

        try{
            FileOutputStream file = new FileOutputStream(filename);
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

    public static void asyncSaveWorld(GameWorld world, String filename){
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
                FileOutputStream file = new FileOutputStream(filename);
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
