package summit.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;

import summit.game.GameWorld;

/**
 * This class provides utility methods to retrieve and upload Game/serialization data
 * to the database. The only class that should access and use these methods is the 
 * {@link GameLoader} class.
 */
public class DBConnection{

    private static String URL;
    private static String USER;
    private static String PASSWORD;
    private static Connection connection;

    //prevent concurrrent access to the database
    private static volatile boolean accessing;
    
    static{
        Properties dbinfo = new Properties();
        
        FileInputStream fis = null;
        FileOutputStream fos = null;
        String init = "";

        try {
            fis = new FileInputStream("db_config.properties");

            dbinfo.load(fis);
            URL = (String)dbinfo.get("db.url");
            USER = (String)dbinfo.get("db.user");
            PASSWORD = (String)dbinfo.get("db.password");
            
            init = (String)dbinfo.get("db.initialized");

            if(init.equals("false")){
                fos = new FileOutputStream("db_config.properties");
                initDB();
                dbinfo.setProperty("db.initialized", "true");
                dbinfo.store(fos, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fos != null){
                    fos.flush();
                    fos.close();
                }
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        connect();
    }

    public static boolean updateSettings(){
        Time.waitWhile((Object obj) -> {
            return accessing;
        });

        accessing = true;
        
        FileInputStream tempSettings = null;
        PreparedStatement ps = null;

        try {
            System.out.println("Updating settings in database...");

            ps = connection.prepareStatement
            ("UPDATE settings SET Settings=?");

            tempSettings = new FileInputStream("settings.properties");

            ps.setString(1, new String(tempSettings.readAllBytes(), StandardCharsets.UTF_8));
            
            ps.executeUpdate();

            System.out.println("Successfully updated game settings in database");
            return true;
        } catch (SQLException | IOException e) {
            System.out.println("Failed to update game settings in database");
            e.printStackTrace();
        }
        finally {
            accessing = false;
            try {
                tempSettings.close();
                ps.close();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static Properties getSettings(){
        Time.waitWhile((Object obj) -> {
            return accessing;
        });

        accessing = true;

        Properties settings = new Properties();
        ByteArrayInputStream tempSettings = null;
        Statement st = null;

        try {
            st = connection.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM settings");

            rs.next();

            //copy properties as a string
            String prop = rs.getString("Settings");

            // prop.replaceAll("\n", ", ");
            //write properties to local file
            
            // System.out.println(prop);

            tempSettings = new ByteArrayInputStream(prop.getBytes());
            settings.load(tempSettings);

            // System.out.println(settings);

            return settings;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        finally {
            accessing = false;
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return settings;
    }

    /**
     * Creates connection to database.
     */
    public static synchronized boolean connect(){
        System.out.println();
        try {
            System.out.println("\nCreating connection to database: " + URL);

            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(true);
            
            System.out.println("Connection successful");
            return true;

        } catch (SQLException e) {
            System.out.println("Failed to connect to database: ");
            if(e.getLocalizedMessage().contains("Unknown database")){
                System.out.println("Could not find database... initializing Summit database");
            }
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Called if the table is not found/ hasn't been created yet
     */
    private static void initDB(){

    }

    /**
     * Creates a new entry in the database for this GameWorld using its {@code SAVE_KEY}
     * 
     * @param saveKey The {@code SAVE_KEY} of the GameWorld
     * 
     * @see GameWorld#getSaveKey()
     */
    public static synchronized void createSave(String saveKey, String saveName){
        Time.waitWhile((Object obj) -> {
            return accessing;
        });

        System.out.println();
        accessing = true;

        PreparedStatement ps = null;

        try {
            if(connection == null){
                System.out.println("Failed to create save \"" + saveKey + "\"");
                return;
            }

            //first check if already exists
            ResultSet check = connection.createStatement().executeQuery(("SELECT GameSave FROM gamedata WHERE SaveKey=\"" + saveKey + "\""));

            if(check.next()){
                System.out.println("\"" + saveKey + "\" already exists, did not create");
                return;
            }
            
            System.out.println("Creating save \"" + saveKey + "\"...");
            ps = connection.prepareStatement("INSERT INTO gamedata VALUES (?, ?, ?, ?, ?)");

            ps.setString(1, saveKey);
            ps.setFloat(2, -1);
            //empty blob
            ps.setBlob(3,  new ByteArrayInputStream(new byte[0]));
            ps.setInt(4,  GameWorld.GAME_NOT_COMPLETED);
            ps.setString(5, saveName);

            ps.execute();
            System.out.println("Successfully created Save \"" + saveKey + "\"");
        } catch (SQLException e) {
            System.out.println("Failed to create save \"" + saveKey + "\"");
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
     * Retrieves all the saved {@code GameWorld} keys and names, and returns a mapped 
     * {@code HashMap<String, String>}
     * 
     * @return A {@code HashMap<String, String>} containing a list of the saved GameWorld
     *          {@code SaveName} (Key) and {@code SaveKey} (Value)
     */
    public static HashMap<String, String> getSaves(){
        Time.waitWhile((Object obj) -> {
            return accessing;
        });

        System.out.println();

        accessing = true;

        HashMap<String, String> savesMap = new HashMap<>();
        Statement st = null;

        try {
            if(connection == null){
                System.out.println("Failed to retrieve game save keys and names");
                return savesMap;
            }

            st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM gamedata");

            while(rs.next()){
                savesMap.put(rs.getString("SaveKey"),
                            rs.getString("SaveName"));
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve game save keys and names");
            e.printStackTrace();
        }
        finally{
            accessing = false;
            try {
                st.close();
            } catch(NullPointerException e){
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return savesMap;
    }

    /**
     * Deletes the entry associated with {@code saveKey} from the database
     * 
     * @param saveKey 
     */
    public static void removeSave(String saveKey) {
        Time.waitWhile((Object obj) -> {
            return accessing;
        });

        System.out.println();
        accessing = true;

        Statement st = null;

        try {
            st = connection.createStatement();
            
            st.executeUpdate("DELETE FROM gamedata WHERE SaveKey=\"" + saveKey + "\"");

            System.out.println("Successfully deleted database entry: \"" + saveKey + "\"");

        } catch (SQLException e) {
            System.out.println("Failed to delete database entry: \"" + saveKey + "\"");
            e.printStackTrace();
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
     * @param saveKey the {@code SAVE_KEY} of the {@code GameWorld} in the database
     * 
     * @return A {@code File} object pointing to {@code gamesaves/temp.txt}.
     *          Returns null if an exception occured.
     * 
     * @see GameWorld#getSaveKey()
     */
    public static synchronized File getSave(String saveKey){
        Time.waitWhile((Object obj) -> {
            return accessing;
        });

        System.out.println();
        accessing = true;

        Statement st = null;
        ResultSet result = null;
        OutputStream out = null;

        File file = null;

        try {
            st = connection.createStatement();

            System.out.println("Retrieving save \"" + saveKey + "\"");

            result = st.executeQuery(("SELECT GameSave FROM gamedata WHERE SaveKey=\"" + saveKey + "\""));

            if(!result.next()){
                throw new IllegalArgumentException("Non-existant SaveKey \"" + saveKey + "\"... please create new database entry");
            }

            Blob blob = result.getBlob("GameSave");
            
            out = new FileOutputStream(GameLoader.tempFile);

            InputStream in = blob.getBinaryStream();
            byte[] buff = in.readAllBytes();    
            in.close();

            out.write(buff);

            System.out.println("Retrieve was successful for \"" + saveKey + "\"");

            file = new File(GameLoader.tempFile);

        } catch(IllegalArgumentException e){
            // e.printStackTrace();
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
     * {@code saveKey} will be deleted
     * 
     * @param saveKey The {@code SAVE_KEY} of the GameWorld being saved 
     * @param gameTime The amount of time spent in-game 
     * @return True if update was successful
     * 
     * @see GameWorld#getSaveKey()
     * @see GameWorld#getElapsedTime()
     */
    public static synchronized boolean updateSave(String saveKey, float gameTime, int completion){
        Time.waitWhile((Object obj) -> {
            return accessing;
        });

        System.out.println();
        accessing = true;

        PreparedStatement ps = null;
        FileInputStream fis = null;

        if(completion == GameWorld.GAME_OVER_PLAYER_DEAD){
            System.out.println("Will not save world with completion status: GameWorld.GAME_OVER_PLAYER_DEAD");
            accessing = false;
            return false;
        }
        
        try {
            System.out.println("Updating database game save \"" + saveKey + "\"");

            if(connection == null){
                System.out.println("Failed to update database game save \"" + saveKey + "\"");
                return false;
            }

            ps = connection.prepareStatement
            ("UPDATE gamedata SET GameTime=?, GameSave=?, GameCompleted=? WHERE SaveKey=\"" + saveKey + "\"");

            fis = new FileInputStream(GameLoader.tempFile);

            ps.setFloat(1, gameTime);
            ps.setBlob(2, fis);
            ps.setInt(3, completion);
            
            ps.execute();

            System.out.println("Successfully updated database game save \"" + saveKey + "\"");
        } catch (SQLNonTransientConnectionException e){
        } catch (SQLException e) {
            System.out.println("Failed to update database game save \"" + saveKey + "\"");
            e.printStackTrace();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } finally{
            accessing = false;
            try {
                fis.close();
                ps.close();
            } catch (NullPointerException e) {
            } catch (IOException e) {   
            } catch (SQLException e) {
                e.printStackTrace();
            } 
        }

        return false;
    }

    public static void closeConnection(){
        System.out.println();
        //wait
        Time.waitWhile((Object obj) -> {
            return accessing;
        });

        try {
            if(connection == null){
                System.out.println("Failed to close database connection");
                return;
            }
            
            connection.close();
            System.out.println("Successfully closed database connection");
        } catch (SQLException e) {
            System.out.println("Failed to close database connection");
            e.printStackTrace();
        }
    }

    public static void main0(String[] args) {
    }
}
