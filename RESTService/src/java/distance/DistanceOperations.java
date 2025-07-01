package distance;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import HTTPHandler.HttpConnectionHandler;

/**
 * REST Web Service for Distance Operations
 */
@Path("distance")
public class DistanceOperations {

    @Context
    private UriInfo context;

    private final HttpConnectionHandler httpHandler = new HttpConnectionHandler();
    private final Gson gson = new Gson();

    /**
     * Retrieves distance and duration between two coordinates
     * @param startLat Latitude of the starting point
     * @param startLon Longitude of the starting point
     * @param endLat Latitude of the destination
     * @param endLon Longitude of the destination
     * @return JSON response with distance and duration information in km and formatted time
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getDistance(
            @QueryParam("startLat") double startLat,
            @QueryParam("startLon") double startLon,
            @QueryParam("endLat") double endLat,
            @QueryParam("endLon") double endLon) {
        
        String apiUrl = String.format(
            "http://router.project-osrm.org/route/v1/driving/%f,%f;%f,%f?overview=false",
            startLon, startLat, endLon, endLat
        );

        try {
            String jsonResponse = httpHandler.sendGetRequest(apiUrl);

            DistanceObject distanceObject = gson.fromJson(jsonResponse, DistanceObject.class);

            if (distanceObject.getRoutes() != null && !distanceObject.getRoutes().isEmpty()) {
                Route route = distanceObject.getRoutes().get(0);

                double distanceKm = route.getDistance() / 1000.0;

                long durationSeconds = Math.round(route.getDuration());
                long hours = durationSeconds / 3600;
                long minutes = (durationSeconds % 3600) / 60;

                JsonObject result = new JsonObject();
                result.addProperty("distance_km", distanceKm);
                result.addProperty("duration", String.format("%d hours %d minutes", hours, minutes));

                return result.toString();
            }
            
            return "{ \"status\": \"error\", \"message\": \"No route data available.\" }";

        } catch (Exception e) {
            return "{ \"status\": \"error\", \"message\": \"Failed to fetch distance data.\" }";
        }
    }
}
