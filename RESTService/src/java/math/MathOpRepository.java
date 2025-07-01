/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package math;

import com.google.gson.Gson;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author brand
 */



@Path("mathrepo")
public class MathOpRepository {
    // Can you think of a better way to keep count?
    // Think of when you have multiple clients sendig requests at the same time.
    private static int count = 0; 
    // A hashmap is used to  save operations and corresponding IDs.
    private static final Map<Integer, MathOp> MathOpStore = new HashMap<>();
    
    private static final Gson gson = new Gson();
  
    public MathOpRepository() {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON) // Can you think of an alternative return type  (and what to return!) in this method?
    public void postOperation(String content) {
           
        // deserialize
        MathOp  operation = gson.fromJson(content, MathOp.class);
        
        // set values of new fields 
        operation.setResult(operation.getX()+operation.getY());
        operation.setId(count);
        
        // save the serialized operation in the hashmap alongside its corresponding ID 
        MathOpStore.put(count, operation);
        
        // increment ID count
        count++; 
    }

    /**
     * This method returns all math operations saved in MathOpStore
     * @return JSON array
    */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getOperations() {
        // Extract all values and save them in a collection
        Collection<MathOp> mathOps = MathOpStore.values();

        // Convert the collection into a JSON string using Gson
        return gson.toJson(mathOps);
    }
  
    /**
     * This method returns one operation from MathOpStore
     * @param id ID of the math operation
     * @return JSON String
    */

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMathOperationByID(@PathParam("id") int id) {
        MathOp operation = MathOpStore.get(id);
        // serialize into JSON before returning the value
        return null;
    }
}
