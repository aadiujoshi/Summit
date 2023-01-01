package summit.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection{

    private static final String URL = "jdbc:mysql://localhost:3306/summitdata";
    private static final String USER = "root";
    private static final String PASSWORD = "djk%f$dj01prS";
    private static Connection connection;

    private static boolean retry;

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

    /**
     * Returns the serialized game save file from the local database
     * 
     * @param saveName Name of the file in the database
     */
    public static synchronized File getDBSave(String saveName){
        
        Statement st = null;
        ResultSet result = null;
        OutputStream out = null;

        try {
            st = connection.createStatement();

            result = st.executeQuery(("SELECT GameSave FROM gamedata WHERE SaveName=\"" + saveName + "\""));
            Blob blob = result.getBlob("gameSave");
            
            out = new FileOutputStream("gamesaves/temp.txt");
            InputStream in = blob.getBinaryStream();

            byte[] buff = in.readAllBytes();            
            out.write(buff);

            return new File("gamesaves/temp.txt");

        } catch (SQLException | IOException e) {
            if(!retry){
                System.out.println("Retrieve failed");
                System.out.println("Retrying...");
                connect();
                getDBSave(saveName);
                retry = true;
            }
            
            e.printStackTrace();
            
        } 
        finally{
            try {
                //close in reverse order of creation incase of npe
                out.close();
                result.close();
                st.close();
            } catch (NullPointerException e) {
            } catch(SQLException | IOException e){
                System.out.println(e);
            }
        }
        
        retry = false;

        return null;
    }
    
    /**
     * Upload game save to database
     * 
     * @param saveName the name of the GameWorld being saved 
     * @param gameTime
     * @param gameSave
     * @return
     */
    public static synchronized boolean updateSave(String saveName, float gameTime, File gameSave){

        Statement st = null;
        ResultSet result = null;

        try {
            st = connection.createStatement();
            String qry = ("SELECT GameSave FROM gamedata WHERE SaveName=\"" + saveName + "\"");
            result = st.executeQuery(qry);
        } catch (SQLException e) {
            
        }
        finally{
            try {
                st.close();
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

    public static void main(String[] args) {
        //test
        getDBSave("hello");
    }
}
