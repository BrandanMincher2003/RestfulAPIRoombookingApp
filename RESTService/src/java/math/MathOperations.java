package math;

import com.google.gson.Gson;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * REST Web Service for Math Operations
 */
@Path("math")
public class MathOperations {

    @Context
    private UriInfo context;

    private final Gson gson = new Gson(); // Gson instance
    private static final AtomicInteger idCounter = new AtomicInteger(1); // Thread-safe ID generator

    /**
     * Creates a new instance of MathOperations
     */
    public MathOperations() {
    }

    /**
     * Addition operation based on query parameters
     *
     * @param x the first number
     * @param y the second number
     * @return a JSON response with the result of the addition
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String addNumbers(@QueryParam("x") int x, @QueryParam("y") int y) {
        // Perform addition
        int result = x + y;

        // Generate a unique id
        int id = idCounter.getAndIncrement();

        // Create a MathOp object
        MathOp mathOp = new MathOp(id, x, y, result);

        // Convert MathOp object to JSON
        return gson.toJson(mathOp);
    }
}
