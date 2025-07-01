import javax.ws.rs.core.Application;
import java.util.Set;

@javax.ws.rs.ApplicationPath("webresources") // Define the base URI path for all resources
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(account.AccountOperations.class);
        resources.add(account.AccountPOST.class);
        resources.add(crime.CrimeOperations.class);
        resources.add(distance.DistanceOperations.class);
        resources.add(math.MathOpRepository.class);
        resources.add(math.MathOperations.class);
        resources.add(postcode.PostCodeOperations.class);
        resources.add(room.RoomOperations.class);
        resources.add(weather.WeatherOperations.class);
    }
}
