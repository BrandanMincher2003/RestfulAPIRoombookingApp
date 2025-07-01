package crime;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import HTTPHandler.HttpConnectionHandler;
import java.lang.reflect.Type;
import java.util.*;

/**
 * REST Web Service for Crime Operations
 */
@Path("crime")
public class CrimeOperations {

    @Context
    private UriInfo context;

    private final HttpConnectionHandler httpHandler = new HttpConnectionHandler();
    private final Gson gson = new Gson();

    /**
     * Retrieves crime data based on latitude, longitude, and date, and calculates category counts.
     *
     * @param lat Latitude as a query parameter
     * @param lon Longitude as a query parameter
     * @param date Date as a query parameter in format YYYY-MM
     * @return JSON response with crime category counts
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getCrimeCategoryCounts(
            @QueryParam("lat") double lat,
            @QueryParam("lon") double lon,
            @QueryParam("date") String date) {

        String apiUrl = String.format(
                "https://data.police.uk/api/crimes-street/all-crime?lat=%f&lng=%f&date=%s",
                lat, lon, date
        );

        try {
            String jsonResponse = httpHandler.sendGetRequest(apiUrl);

            Type crimeListType = new TypeToken<List<CrimeObject>>() {}.getType();
            List<CrimeObject> crimes = gson.fromJson(jsonResponse, crimeListType);

            Map<String, Integer> categoryCounts = new HashMap<>();
            for (CrimeObject crime : crimes) {
                categoryCounts.merge(crime.getCategory(), 1, Integer::sum);
            }

            return gson.toJson(categoryCounts);

        } catch (Exception e) {
            return "{ \"status\": \"error\", \"message\": \"Failed to fetch or process crime data\" }";
        }
    }
}
