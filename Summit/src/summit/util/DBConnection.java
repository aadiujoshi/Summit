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

public class DBConnection{

    private static final String URL = "jdbc:mysql://localhost:3306/summitdata";
    private static final String USER = "root";
    private static final String PASSWORD = "djk%f$dj01prS";
    private static Connection connection;

    // private static boolean retry;

    static{
        connect();
    }

    /**
     * Creates connection to database
     */
    public static synchronized void connect(){
        try {
            System.out.println("\nCreating connection to database: " + URL);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection successful");
        } catch (SQLException e) {
            System.out.print("Failed to connect to database: ");
            e.printStackTrace();
        }
    }

    public static synchronized void createSave(String saveName){
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement("INSERT INTO gamedata VALUES (?, ?, ?)");

            ps.setString(1, saveName);
            ps.setFloat(2, -1);

            //empty blob
            ps.setBlob(3,  new ByteArrayInputStream(new byte[0]));

            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the serialized game save file from the local database
     * 
     * @param saveName Name of the file in the database
     */
    public static synchronized File getSave(String saveName){
        
        Statement st = null;
        ResultSet result = null;
        OutputStream out = null;

        try {
            st = connection.createStatement();

            result = st.executeQuery(("SELECT GameSave FROM gamedata WHERE SaveName=\"" + saveName + "\""));

            //if empty create new entry in database
            if(!result.next()){
                throw new IllegalArgumentException("Non-existant save name... please create new database entry");
            }

            Blob blob = result.getBlob("GameSave");
            
            out = new FileOutputStream(GameLoader.tempFile);

            InputStream in = blob.getBinaryStream();
            byte[] buff = in.readAllBytes();    

            out.write(buff);

            return new File(GameLoader.tempFile);

        } catch(IllegalArgumentException e){
            e.printStackTrace();
        } catch (IOException e) { 
            e.printStackTrace();
        } catch(SQLException e){
            System.out.println("Retrieve failed");
            System.out.println("Re-establishing connection... please retry command");
            connect();

            e.printStackTrace();
        }
        finally{
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
        
        return null;
    }
    
    /**
     * Upload game save to database. Serialized data is copied from the temp.txt file 
     * to a BLOB, to be uploaded to the database.
     * 
     * @param saveName The name of the GameWorld being saved 
     * @param gameTime The amount of time spent in-game 
     * @return True if update was successful
     * 
     * @see GameWorld#getSaveName()
     * @see GameWorld#getGametime()
     */
    public static synchronized boolean updateSave(String saveName, float gameTime){

        PreparedStatement ps = null;
        
        try {
            ps = connection.prepareStatement
            ("UPDATE gamedata SET GameTime=?, GameSave=? WHERE SaveName=\"" + saveName + "\"");

            ps.setFloat(1, gameTime);
            ps.setBlob(2, new FileInputStream(GameLoader.tempFile));
            
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } finally{
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static void closeConnection(){
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Failed to close database connection");
            e.printStackTrace();
        }
    }

    public static void main0(String[] args) {
        //test
        getSave("hello");
    }
}
