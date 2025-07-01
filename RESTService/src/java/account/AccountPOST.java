package account;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * REST Web Service for handling account creation.
 */
@Path("accountpost")
public class AccountPOST {

    private static final String FILE_PATH = "accounts.json"; // Path to the JSON file
    private final Gson gson = new Gson();

    /**
     * Handles the creation of a new account.
     *
     * @param content JSON content containing the username and password.
     * @return Response indicating success or failure.
     */
@POST
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response createAccount(String content) {
    try {
        Type mapType = new TypeToken<Map<String, String>>() {}.getType();
        Map<String, String> requestData = gson.fromJson(content, mapType);
        String username = requestData.get("username");
        String password = requestData.get("password");

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"status\": \"error\", \"message\": \"Username and password must be provided.\"}")
                    .build();
        }

        // Load existing accounts
        List<AccountObject> accounts = loadAccounts();

        // Check for duplicate usernames
        boolean exists = accounts.stream().anyMatch(account -> account.getUsername().equals(username));
        if (exists) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"status\": \"error\", \"message\": \"Username already exists.\"}")
                    .build();
        }

        AccountObject newAccount = new AccountObject(username, password);

        accounts.add(newAccount);

        saveAccounts(accounts);

        return Response.status(Response.Status.CREATED)
                .entity("{\"status\": \"success\", \"message\": \"Account created successfully.\"}")
                .build();

    } catch (Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"status\": \"error\", \"message\": \"An error occurred: " + e.getMessage() + "\"}")
                .build();
    }
}

    /**
     * Loads accounts from the JSON file.
     *
     * @return List of AccountObject instances.
     * @throws IOException if the file cannot be read.
     */
    private List<AccountObject> loadAccounts() throws IOException {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return new ArrayList<>(); // Return an empty list if the file doesn't exist
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Type listType = new TypeToken<List<AccountObject>>() {}.getType();
            return gson.fromJson(reader, listType);
        }
    }

    /**
     * Saves accounts to the JSON file.
     *
     * @param accounts List of AccountObject instances to save.
     * @throws IOException if the file cannot be written.
     */
    private void saveAccounts(List<AccountObject> accounts) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            gson.toJson(accounts, writer);
        }
    }
}
