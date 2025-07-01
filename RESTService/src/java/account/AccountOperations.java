package account;

import com.google.gson.Gson;
import com.mongodb.client.*;
import org.bson.Document;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

@Path("account")
public class AccountOperations {

    private final Gson gson = new Gson();

    private static final String MONGO_URI = "exluded";
    private static final String DATABASE_NAME = "n1146581";
    private static final String COLLECTION_NAME = "Accounts";

    /**
     * Fetches all accounts with their applications from the MongoDB database.
     *
     * @param context the ServletContext for dynamically accessing the MongoDB configuration.
     * @return JSON representation of all accounts with their applications.
     */
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllAccounts(@Context ServletContext context) {
        try (MongoClient mongoClient = MongoClients.create(MONGO_URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);

            MongoCollection<Document> accountsCollection = database.getCollection(COLLECTION_NAME);

            List<AccountObject> accounts = new ArrayList<>();
            try (MongoCursor<Document> accountCursor = accountsCollection.find().iterator()) {
                while (accountCursor.hasNext()) {
                    Document accountDoc = accountCursor.next();

                    AccountObject account = new AccountObject();
                    account.setUsername(accountDoc.getString("username"));
                    account.setPassword(accountDoc.getString("password"));
                    account.setRole(accountDoc.getString("role"));

                    List<ApplicationObject> applications = new ArrayList<>();
                    List<Document> appDocs = (List<Document>) accountDoc.get("applications");
                    if (appDocs != null) {
                        for (Document appDoc : appDocs) {
                            ApplicationObject application = new ApplicationObject();
                            application.setApplicationId(appDoc.getInteger("application_id"));
                            application.setRoomId(appDoc.getInteger("room_id"));
                            application.setStatus(appDoc.getString("status"));
                            application.setApplicationDate(appDoc.getString("application_date"));
                            application.setDecisionDate(appDoc.getString("decision_date"));
                            applications.add(application);
                        }
                    }

                    account.setApplications(applications);

                    accounts.add(account);
                }
            }

            return gson.toJson(accounts);

        } catch (Exception e) {
            e.printStackTrace();
            return "{ \"status\": \"error\", \"message\": \"Error fetching data: " + e.getMessage() + "\" }";
        }
    }

    /**
     * Adds a new account to the MongoDB database.
     *
     * @param content JSON representation of the account to be added.
     * @return A success or error message.
     */
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String addAccount(String content) {
        try (MongoClient mongoClient = MongoClients.create(MONGO_URI)) {
            AccountObject account = gson.fromJson(content, AccountObject.class);

            Document accountDoc = new Document()
                    .append("username", account.getUsername())
                    .append("password", account.getPassword())
                    .append("role", account.getRole());

            if (account.getApplications() != null) {
                List<Document> appDocs = new ArrayList<>();
                for (ApplicationObject app : account.getApplications()) {
                    Document appDoc = new Document()
                            .append("application_id", app.getApplicationId())
                            .append("room_id", app.getRoomId())
                            .append("status", app.getStatus())
                            .append("application_date", app.getApplicationDate())
                            .append("decision_date", app.getDecisionDate());
                    appDocs.add(appDoc);
                }
                accountDoc.append("applications", appDocs);
            }

            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> accountsCollection = database.getCollection(COLLECTION_NAME);
            accountsCollection.insertOne(accountDoc);

            return "{ \"status\": \"success\", \"message\": \"Account added successfully.\" }";

        } catch (Exception e) {
            e.printStackTrace();
            return "{ \"status\": \"error\", \"message\": \"Error adding account: " + e.getMessage() + "\" }";
        }
    }

    /**
     * Updates an existing account in the MongoDB database.
     *
     * @param content JSON representation of the account with updated details.
     * @return A success or error message.
     */
    @PUT
    @Path("/edit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String editAccount(String content) {
        try (MongoClient mongoClient = MongoClients.create(MONGO_URI)) {
            AccountObject updatedAccount = gson.fromJson(content, AccountObject.class);

            if (updatedAccount.getUsername() == null || updatedAccount.getUsername().isEmpty()) {
                return "{ \"status\": \"error\", \"message\": \"Username is required to edit the account.\" }";
            }

            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> accountsCollection = database.getCollection(COLLECTION_NAME);

            Document updatedFields = new Document()
                    .append("password", updatedAccount.getPassword())
                    .append("role", updatedAccount.getRole());

            if (updatedAccount.getApplications() != null) {
                List<Document> appDocs = new ArrayList<>();
                for (ApplicationObject app : updatedAccount.getApplications()) {
                    Document appDoc = new Document()
                            .append("application_id", app.getApplicationId())
                            .append("room_id", app.getRoomId())
                            .append("status", app.getStatus())
                            .append("application_date", app.getApplicationDate())
                            .append("decision_date", app.getDecisionDate());
                    appDocs.add(appDoc);
                }
                updatedFields.append("applications", appDocs);
            }

            Document query = new Document("username", updatedAccount.getUsername()); // Identify the account by username
            Document update = new Document("$set", updatedFields);

            accountsCollection.updateOne(query, update);

            return "{ \"status\": \"success\", \"message\": \"Account updated successfully.\" }";

        } catch (Exception e) {
            e.printStackTrace();
            return "{ \"status\": \"error\", \"message\": \"Error updating account: " + e.getMessage() + "\" }";
        }
    }
}
