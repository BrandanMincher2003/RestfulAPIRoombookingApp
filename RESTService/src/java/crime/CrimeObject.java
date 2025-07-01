/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crime;

/**
 *
 * @author brand
 */

public class CrimeObject {
    private String category;
    private String location_type;
    private Location location;
    private String context;
    private String persistent_id;
    private String id;
    private OutcomeStatus outcome_status;
    private String month;

    // Nested class for Location
    public static class Location {
        private String latitude;
        private String longitude;
        private Street street;

        // Nested class for Street
        public static class Street {
            private int id;
            private String name;

            // Getters
            public int getId() { return id; }
            public String getName() { return name; }
        }

        // Getters
        public String getLatitude() { return latitude; }
        public String getLongitude() { return longitude; }
        public Street getStreet() { return street; }
    }

    // Nested class for OutcomeStatus
    public static class OutcomeStatus {
        private String category;
        private String date;

        // Getters
        public String getCategory() { return category; }
        public String getDate() { return date; }
    }

    // Getters
    public String getCategory() { return category; }
    public String getLocationType() { return location_type; }
    public Location getLocation() { return location; }
    public String getContext() { return context; }
    public String getPersistentId() { return persistent_id; }
    public String getId() { return id; }
    public OutcomeStatus getOutcomeStatus() { return outcome_status; }
    public String getMonth() { return month; }
}
