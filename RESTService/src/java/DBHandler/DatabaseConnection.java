/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DBHandler;

/**
 *
 * @author brand
 * 
 * 
 * n1146581
 * 4KQ0GVPmTdGmkNz5
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;




public class DatabaseConnection {

    public Connection connect() {
        Connection conn = null;
        try {
            
            String url = "jdbc:sqlite:C:/Users/brand/Documents/University/Year_3/ServiceCentricCloudComputing/apache-tomcat-9.0.96/apache-tomcat-9.0.96/webapps/RoomsDB.db";
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite database established.");

            createRoomsTable(conn);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return conn;
    }

private void createRoomsTable(Connection conn) {
    String createTableSQL = "CREATE TABLE IF NOT EXISTS rooms ("
            + "id INTEGER PRIMARY KEY,"
            + "name TEXT NOT NULL,"
            + "city TEXT,"
            + "county TEXT,"
            + "postcode TEXT,"
            + "latitude REAL," // Added latitude
            + "longitude REAL," // Added longitude
            + "furnished BOOLEAN,"
            + "amenities TEXT," // storing as comma-separated values
            + "live_in_landlord BOOLEAN,"
            + "shared_with INTEGER,"
            + "bills_included BOOLEAN,"
            + "bathroom_shared BOOLEAN,"
            + "price_per_month_gbp REAL,"
            + "availability_date TEXT," // using TEXT format for date
            + "spoken_languages TEXT" // storing as comma-separated values
            + ");";
    
    try (Statement stmt = conn.createStatement()) {
        stmt.execute(createTableSQL);
        System.out.println("Rooms table created or already exists.");
    } catch (SQLException e) {
        System.out.println("Error creating table: " + e.getMessage());
    }
}


    public static void main(String[] args) {
        DatabaseConnection dbConnector = new DatabaseConnection();
        dbConnector.connect();
    }
}   
