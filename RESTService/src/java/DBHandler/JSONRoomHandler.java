package DBHandler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import room.RoomObject;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JSONRoomHandler {

    private final List<RoomObject> rooms;

    public JSONRoomHandler() {
    try {
        String filePath = getClass().getClassLoader().getResource("resources/rooms.json").getPath();

        try (FileReader reader = new FileReader(filePath)) {
            Type type = new TypeToken<Map<String, List<RoomObject>>>() {}.getType();
            Map<String, List<RoomObject>> data = new Gson().fromJson(reader, type);

            rooms = data.get("rooms");
            if (rooms == null) {
                throw new RuntimeException("No 'rooms' key found in the JSON file.");
            }
        }

        System.out.println("Rooms loaded successfully from JSON.");
    } catch (Exception e) {
        throw new RuntimeException("Failed to load rooms JSON file: " + e.getMessage());
    }
}


    /**
     * Fetches a room by ID from the JSON file.
     * @param id the ID of the room to retrieve
     * @return RoomObject if found, or null if no room with the specified ID exists
     */
    public RoomObject getRoomById(int id) {
        Optional<RoomObject> room = rooms.stream().filter(r -> r.getId() == id).findFirst();
        return room.orElse(null);
    }
    
    
    public List<RoomObject> getAllRooms() {
        return rooms;
    }
}
