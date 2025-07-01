package room;

import com.google.gson.Gson;
import com.mongodb.client.*;
import org.bson.Document;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("room")
public class RoomOperations {

    private final Gson gson = new Gson();

    private static final String MONGO_URI = "excluded";
    private static final String DATABASE_NAME = "n1146581";
    private static final String COLLECTION_NAME = "Rooms";

    /**
     * Fetches all rooms from the MongoDB database.
     *
     * @return JSON representation of all rooms.
     */
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllRooms() {
        try (MongoClient mongoClient = MongoClients.create(MONGO_URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> roomsCollection = database.getCollection(COLLECTION_NAME);

            List<RoomObject> rooms = new ArrayList<>();
            try (MongoCursor<Document> cursor = roomsCollection.find().iterator()) {
                while (cursor.hasNext()) {
                    Document roomDoc = cursor.next();

                    RoomObject room = mapToRoomObject(roomDoc);
                    rooms.add(room);
                }
            }

            return gson.toJson(rooms);

        } catch (Exception e) {
            e.printStackTrace();
            return "{ \"status\": \"error\", \"message\": \"Error fetching data: " + e.getMessage() + "\" }";
        }
    }

    /**
     * Fetches rooms by ID or city from the MongoDB database.
     *
     * @param id   The ID of the room to fetch (optional).
     * @param city The city of the rooms to fetch (optional).
     * @return JSON representation of the room(s) or an error message if not found.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getRoomByIdOrCity(@QueryParam("id") Integer id, @QueryParam("city") String city) {
        try (MongoClient mongoClient = MongoClients.create(MONGO_URI)) {
            
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> roomsCollection = database.getCollection(COLLECTION_NAME);

            Document query = new Document();
            if (id != null) {
                query.append("id", id);
            } else if (city != null && !city.isEmpty()) {
                query.append("location.city", city);
            } else {
                return "{ \"status\": \"error\", \"message\": \"Please provide either an id or a city.\" }";
            }

            List<RoomObject> rooms = new ArrayList<>();
            try (MongoCursor<Document> cursor = roomsCollection.find(query).iterator()) {
                while (cursor.hasNext()) {
                    Document roomDoc = cursor.next();

                    RoomObject room = mapToRoomObject(roomDoc);
                    rooms.add(room);
                }
            }

            if (rooms.isEmpty()) {
                return "{ \"status\": \"error\", \"message\": \"No rooms found for the given criteria.\" }";
            }

            return gson.toJson(rooms);

        } catch (Exception e) {
            e.printStackTrace();
            return "{ \"status\": \"error\", \"message\": \"Error fetching data: " + e.getMessage() + "\" }";
        }
    }

    /**
     * Maps a MongoDB document to a RoomObject.
     *
     * @param roomDoc The MongoDB document to map.
     * @return The mapped RoomObject.
     */
    private RoomObject mapToRoomObject(Document roomDoc) {
        RoomObject room = new RoomObject();
        room.setId(((Number) roomDoc.get("id")).intValue()); // Safely handle Integer or Double
        room.setName(roomDoc.getString("name"));

        Document locationDoc = roomDoc.get("location", Document.class);
        if (locationDoc != null) {
            RoomObject.Location location = new RoomObject.Location();
            location.setCity(locationDoc.getString("city"));
            location.setCounty(locationDoc.getString("county"));
            location.setPostcode(locationDoc.getString("postcode"));
            room.setLocation(location);
        }

        Document detailsDoc = roomDoc.get("details", Document.class);
        if (detailsDoc != null) {
            RoomObject.Details details = new RoomObject.Details();
            details.setFurnished(detailsDoc.getBoolean("furnished", false));
            details.setAmenities((List<String>) detailsDoc.get("amenities"));
            details.setLiveInLandlord(detailsDoc.getBoolean("live_in_landlord", false));
            details.setSharedWith(((Number) detailsDoc.get("shared_with")).intValue()); // Safely handle Integer or Double
            details.setBillsIncluded(detailsDoc.getBoolean("bills_included", false));
            details.setBathroomShared(detailsDoc.getBoolean("bathroom_shared", false));
            room.setDetails(details);
        }

        room.setPricePerMonthGbp(((Number) roomDoc.get("price_per_month_gbp")).doubleValue()); // Safely handle Integer or Double
        room.setAvailabilityDate(roomDoc.getString("availability_date"));
        room.setSpokenLanguages((List<String>) roomDoc.get("spoken_languages"));

        return room;
    }
}
