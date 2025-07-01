package room;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RoomObject {
    private int id;
    private String name;

    private Location location;
    private Details details;

    @SerializedName("price_per_month_gbp")
    private double pricePerMonthGbp;

    @SerializedName("availability_date")
    private String availabilityDate;

    @SerializedName("spoken_languages")
    private List<String> spokenLanguages;

    // Nested Location class
    public static class Location {
        private String city;
        private String county;
        private String postcode;

        // Getters and Setters
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }

        public String getCounty() { return county; }
        public void setCounty(String county) { this.county = county; }

        public String getPostcode() { return postcode; }
        public void setPostcode(String postcode) { this.postcode = postcode; }
    }

    // Nested Details class
    public static class Details {
        private boolean furnished;

        private List<String> amenities;

        @SerializedName("live_in_landlord")
        private boolean liveInLandlord;

        @SerializedName("shared_with")
        private int sharedWith;

        @SerializedName("bills_included")
        private boolean billsIncluded;

        @SerializedName("bathroom_shared")
        private boolean bathroomShared;

        // Getters and Setters
        public boolean isFurnished() { return furnished; }
        public void setFurnished(boolean furnished) { this.furnished = furnished; }

        public List<String> getAmenities() { return amenities; }
        public void setAmenities(List<String> amenities) { this.amenities = amenities; }

        public boolean isLiveInLandlord() { return liveInLandlord; }
        public void setLiveInLandlord(boolean liveInLandlord) { this.liveInLandlord = liveInLandlord; }

        public int getSharedWith() { return sharedWith; }
        public void setSharedWith(int sharedWith) { this.sharedWith = sharedWith; }

        public boolean isBillsIncluded() { return billsIncluded; }
        public void setBillsIncluded(boolean billsIncluded) { this.billsIncluded = billsIncluded; }

        public boolean isBathroomShared() { return bathroomShared; }
        public void setBathroomShared(boolean bathroomShared) { this.bathroomShared = bathroomShared; }
    }

    // Getters and setters for RoomObject
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public Details getDetails() { return details; }
    public void setDetails(Details details) { this.details = details; }

    public double getPricePerMonthGbp() { return pricePerMonthGbp; }
    public void setPricePerMonthGbp(double pricePerMonthGbp) { this.pricePerMonthGbp = pricePerMonthGbp; }

    public String getAvailabilityDate() { return availabilityDate; }
    public void setAvailabilityDate(String availabilityDate) { this.availabilityDate = availabilityDate; }

    public List<String> getSpokenLanguages() { return spokenLanguages; }
    public void setSpokenLanguages(List<String> spokenLanguages) { this.spokenLanguages = spokenLanguages; }
}
 