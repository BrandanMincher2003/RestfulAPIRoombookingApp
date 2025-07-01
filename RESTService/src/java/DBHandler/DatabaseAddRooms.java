/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DBHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseAddRooms {

    private final Connection connection;

    public DatabaseAddRooms(Connection connection) {
        this.connection = connection;
    }

    public void addRoom(int id, String name, String city, String county, String postcode, boolean furnished,
                        String amenities, boolean liveInLandlord, int sharedWith, boolean billsIncluded,
                        boolean bathroomShared, double pricePerMonthGbp, String availabilityDate, String spokenLanguages) {
        
        String insertSQL = "INSERT INTO rooms (id, name, city, county, postcode, furnished, amenities, live_in_landlord, "
                + "shared_with, bills_included, bathroom_shared, price_per_month_gbp, availability_date, spoken_languages) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, city);
            pstmt.setString(4, county);
            pstmt.setString(5, postcode);
            pstmt.setBoolean(6, furnished);
            pstmt.setString(7, amenities);
            pstmt.setBoolean(8, liveInLandlord);
            pstmt.setInt(9, sharedWith);
            pstmt.setBoolean(10, billsIncluded);
            pstmt.setBoolean(11, bathroomShared);
            pstmt.setDouble(12, pricePerMonthGbp);
            pstmt.setString(13, availabilityDate);
            pstmt.setString(14, spokenLanguages);

            pstmt.executeUpdate();
            System.out.println("Room added to the database successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding room to database: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        DatabaseConnection dbConnector = new DatabaseConnection();
        Connection conn = dbConnector.connect();
        
        if (conn != null) {
            DatabaseAddRooms adder = new DatabaseAddRooms(conn);

            adder.addRoom(1, "Cozy Room in Shared House", "Nottingham", "Nottinghamshire", "NG1 5AA", true,
                    "WiFi,Heating,Kitchen", false, 3, true, true, 500.0, "2024-07-01", "English,Spanish");
            adder.addRoom(2, "Spacious Room in Modern Apartment", "London", "Greater London", "E1 6AN", true,
                    "WiFi,Gym,Laundry", false, 2, false, false, 850.0, "2024-08-15", "English,French");

            adder.addRoom(3, "Affordable Room in Friendly House", "Manchester", "Greater Manchester", "M1 2AB", true,
                    "WiFi,Heating,Garden", true, 4, true, true, 450.0, "2024-06-01", "English,Mandarin");

            adder.addRoom(4, "Luxury Room in Central Location", "Edinburgh", "Midlothian", "EH1 1BB", true,
                    "WiFi,Heating,Parking", false, 1, false, false, 950.0, "2024-09-01", "English");

            adder.addRoom(5, "Quiet Room in Suburban House", "Bristol", "Bristol", "BS1 5TR", false,
                    "WiFi,Heating,Parking", true, 2, true, true, 600.0, "2024-05-15", "English,Italian");

            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}
