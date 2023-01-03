package summit.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import summit.game.GameWorld;

/**
 * This class provides utility methods to retrieve and upload Game/serialization data
 * to the database. The only class that should access and use these methods is the 
 * {@link GameLoader} class.
 */
public class DBConnection{

    private static final String URL = "jdbc:mysql://localhost:3306/summitdata";
    private static final String USER = "root";
    private static final String PASSWORD = "djk%f$dj01prS";
    private static Connection connection;

    private static volatile boolean accessing;

    // private static boolean retry;

    static{
        connect();
    }

    /**
     * Creates connection to database.
     */
    public static synchronized void connect(){
        try {
            System.out.println("\nCreating connection to database: " + URL);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(true);
            System.out.println("Connection successful");
        } catch (SQLException e) {
            System.out.print("Failed to connect to database: ");
            e.printStackTrace();
        }
    }

    /**
     * Creates a new entry in the database for this GameWorld using its save name
     * 
     * @param saveName The save name of the GameWorld
     * 
     * @see GameWorld#getSaveName()
     */
    public static synchronized void createSave(String saveName){
        accessing = true;

        PreparedStatement ps = null;

        try {
            //first check if already exists
            ResultSet check = connection.createStatement().executeQuery(("SELECT GameSave FROM gamedata WHERE SaveName=\"" + saveName + "\""));

            if(check.next()){
                System.out.println("\"" + saveName + "\" already exists, did not create");
                return;
            }
            
            System.out.println("Creating save \"" + saveName + "\"...");
            ps = connection.prepareStatement("INSERT INTO gamedata VALUES (?, ?, ?, ?)");

            ps.setString(1, saveName);
            ps.setFloat(2, -1);

            //empty blob
            ps.setBlob(3,  new ByteArrayInputStream(new byte[0]));
            ps.setInt(4,  GameWorld.GAME_NOT_COMPLETED);

            ps.execute();
            System.out.println("Save \"" + saveName + "\" successfully created");
        } catch (SQLException e) {
            System.out.println("Failed to create save \"" + saveName + "\"");
            e.printStackTrace();
        } finally {
            accessing = false;
            try {
                ps.close();
            } catch(NullPointerException e){
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Deletes the entry associated with {@code saveName} from the database
     * 
     * @param saveName 
     */
    public static void removeSave(String saveName) {
        accessing = true;

        Statement st = null;

        try {
            st = connection.createStatement();

            st.executeQuery("DELETE FROM gamedata WHERE SaveName=\"" + saveName + "\"");

            System.out.println("Successfully deleted database entry: \"" + saveName + "\"");

        } catch (SQLException e) {
            System.out.println("Failed to delete database entry: \"" + saveName + "\"");
        } 
        finally{
            accessing = false;

            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the serialized game save file from the local database. 
     * It first retrieves the {@code BLOB} which contains a serialized
     * byte stream, then writes it to the {@code gamesaves/temp.txt} file
     * to be deserialized by the {@code GameLoader}.
     * 
     * @param saveName the {@code SAVE_NAME} of the {@code GameWorld} in the database
     * 
     * @return A {@code File} object pointing to {@code gamesaves/temp.txt}.
     *          Returns null if an exception occured.
     * 
     * @see GameWorld#getSaveName()
     */
    public static synchronized File getSave(String saveName){
        accessing = true;

        Statement st = null;
        ResultSet result = null;
        OutputStream out = null;

        File file = null;

        try {
            st = connection.createStatement();

            System.out.println("Retrieving save \"" + saveName + "\"");

            result = st.executeQuery(("SELECT GameSave FROM gamedata WHERE SaveName=\"" + saveName + "\""));

            if(!result.next()){
                throw new IllegalArgumentException("Non-existant save name \"" + saveName + "\"... please create new database entry");
            }

            Blob blob = result.getBlob("GameSave");
            
            out = new FileOutputStream(GameLoader.tempFile);

            InputStream in = blob.getBinaryStream();
            byte[] buff = in.readAllBytes();    

            out.write(buff);

            System.out.println("Retrieve successful for \"" + saveName + "\"");

            file = new File(GameLoader.tempFile);

        } catch(IllegalArgumentException e){
            e.printStackTrace();
        } catch (IOException e){ 
            e.printStackTrace();
        } catch(SQLException e){
            System.out.println("Retrieve failed");
            System.out.println("Re-establishing connection... please retry command");
            connect();

            e.printStackTrace();
        }
        finally{
            accessing = false;

            try {
                //close in reverse order of creation incase of null
                out.close();
                result.close();
                st.close();
            } catch (NullPointerException e) {
            } catch(SQLException | IOException e){
                e.printStackTrace();
            }
        }
        
        return file;
    }
    
    /**
     * Upload game save to database. Serialized data is copied from the {@code gamesaves/temp.txt} file 
     * to a {@code BLOB}, to be uploaded to the database. However, if 
     * {@code completion == GameWorld.GAME_OVER_PLAYER_DEAD} is true, the database entry associated with 
     * {@code saveName} will be deleted
     * 
     * @param saveName The name of the GameWorld being saved 
     * @param gameTime The amount of time spent in-game 
     * @return True if update was successful
     * 
     * @see GameWorld#getSaveName()
     * @see GameWorld#getElapsedTime()
     */
    public static synchronized boolean updateSave(String saveName, float gameTime, int completion){
        accessing = true;

        PreparedStatement ps = null;
        
        if(completion == GameWorld.GAME_OVER_PLAYER_DEAD){
            System.out.println("Will not save world with completion status: GameWorld.GAME_OVER_PLAYER_DEAD");
        }
        
        try {
            System.out.println("Updating database game save \"" + saveName + "\"");

            ps = connection.prepareStatement
            ("UPDATE gamedata SET GameTime=?, GameSave=?, GameCompleted=? WHERE SaveName=\"" + saveName + "\"");

            ps.setFloat(1, gameTime);
            ps.setBlob(2, new FileInputStream(GameLoader.tempFile));
            ps.setInt(3, completion);
            
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } finally{
            accessing = false;
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static void closeConnection(){
        //wait
        while(accessing){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Failed to close database connection");
            e.printStackTrace();
        }
    }

    public static void main0(String[] args) {
    }
}
