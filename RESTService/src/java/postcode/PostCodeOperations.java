package postcode;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import HTTPHandler.HttpConnectionHandler;

/* REST Web Service for Postcode Operations
 */
@Path("postcode")
public class PostCodeOperations {

    @Context
    private UriInfo context;

    private final HttpConnectionHandler httpHandler = new HttpConnectionHandler();
    private final Gson gson = new Gson();

    /* Retrieves location data for a given postcode
     * @param postcode the postcode to retrieve data for
     * @return JSON response with location data
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@QueryParam("postcode") String postcode) {
        if (postcode == null || postcode.isEmpty()) {
            return "{ \"status\": \"error\", \"message\": \"Please provide a valid postcode.\" }";
        }

        postcode = postcode.replace(" ", "+");
        String apiUrl = "https://api.getthedata.com/postcode/" + postcode;

        try {
            String jsonResponse = httpHandler.sendGetRequest(apiUrl);

            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            JsonObject dataObject = jsonObject.getAsJsonObject("data");

            PostcodeObject postcodeObject = gson.fromJson(dataObject, PostcodeObject.class);

                return gson.toJson(postcodeObject);

        } catch (Exception e) {
            return "{ \"status\": \"error\", \"message\": \"Failed to fetch postcode data. Error: " + e.getMessage() + "\" }";
        }
    }
}