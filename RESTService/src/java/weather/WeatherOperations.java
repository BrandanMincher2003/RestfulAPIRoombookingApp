package weather;

import com.google.gson.Gson;
import HTTPHandler.HttpConnectionHandler;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * REST Web Service for Weather Operations
 */
@Path("weather")
public class WeatherOperations {

    @Context
    private UriInfo context;

    private final HttpConnectionHandler httpHandler = new HttpConnectionHandler();
    private final Gson gson = new Gson();

    private final String primaryApiUrl = "https://www.7timer.info/bin/civillight.php";
    private final String backupApiUrl = "https://api.open-meteo.com/v1/forecast";

    /**
     * Retrieves weather data from the primary or backup API.
     *
     * @param lat Latitude as a query parameter.
     * @param lon Longitude as a query parameter.
     * @return JSON response with weather data.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@QueryParam("lat") double lat, @QueryParam("lon") double lon) {
        try {
            return fetchPrimaryApiData(primaryApiUrl, lat, lon);
        } catch (Exception primaryApiException) {
            System.err.println("Primary API failed: " + primaryApiException.getMessage());

            try {
                return fetchBackupApiData(backupApiUrl, lat, lon);
            } catch (Exception backupApiException) {
                System.err.println("Backup API failed: " + backupApiException.getMessage());
                return "{ \"status\": \"error\", \"message\": \"Failed to fetch weather data from both APIs.\" }";
            }
        }
    }

    /**
     * Fetches weather data from the primary API.
     *
     * @param apiUrl The URL of the primary API.
     * @param lat    Latitude of the location.
     * @param lon    Longitude of the location.
     * @return JSON response with weather data.
     * @throws Exception if fetching data fails.
     */
    private String fetchPrimaryApiData(String apiUrl, double lat, double lon) throws Exception {
        String fullUrl = apiUrl + "?lat=" + lat + "&lon=" + lon + "&unit=metric&output=json";

        String jsonResponse = httpHandler.sendGetRequest(fullUrl);

        WeatherObject weatherObject = gson.fromJson(jsonResponse, WeatherObject.class);

        int todayDate = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        for (WeatherData data : weatherObject.getDataseries()) {
            if (data.getDate() == todayDate) {
                return gson.toJson(data);
            }
        }

        throw new Exception("Today's weather data not available.");
    }

    /**
     * Fetches weather data from the backup API.
     *
     * @param apiUrl The URL of the backup API.
     * @param lat    Latitude of the location.
     * @param lon    Longitude of the location.
     * @return JSON response with weather data.
     * @throws Exception if fetching data fails.
     */
    private String fetchBackupApiData(String apiUrl, double lat, double lon) throws Exception {
        String fullUrl = apiUrl + "?latitude=" + lat + "&longitude=" + lon + "&current_weather=true";
        String jsonResponse = httpHandler.sendGetRequest(fullUrl);

        BackupWeatherObject backupWeatherObject = gson.fromJson(jsonResponse, BackupWeatherObject.class);

        BackupWeatherData currentWeather = backupWeatherObject.getCurrentWeather();

        WeatherData mappedData = new WeatherData();

        mappedData.setDate(Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));

        String weatherDescription;
        switch (currentWeather.getWeatherCode()) {
            case 51: // Example: Light rain
                weatherDescription = "lightrain";
                break;
            case 1: // Example: Clear weather
                weatherDescription = "clear";
                break;
            default:
                weatherDescription = "unknown";
        }
        mappedData.setWeather(weatherDescription);

        Temp2m temp = new Temp2m();
        temp.setMax((int) currentWeather.getTemperature());
        temp.setMin((int) currentWeather.getTemperature());
        mappedData.setTemp2m(temp);

        int windSpeedInMetersPerSecond = (int) Math.round(currentWeather.getWindspeed() / 3.6);
        mappedData.setWind10m_max(windSpeedInMetersPerSecond);

        return gson.toJson(mappedData);
    }
}
