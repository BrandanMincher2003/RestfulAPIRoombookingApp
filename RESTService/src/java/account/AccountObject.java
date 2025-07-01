/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package account;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing an account for storage and operations.
 */
public class AccountObject {
    private String username;  // Account username
    private String password;  // Account password
    private String role;      // Role (default: "user")
    private List<ApplicationObject> applications; // List of applications for the account

    // Default constructor (required for deserialization)
    public AccountObject() {
        this.role = "user"; // Default role
        this.applications = new ArrayList<>(); // Default empty applications list
    }

    // Parameterized constructor
    public AccountObject(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = "user"; // Default role
        this.applications = new ArrayList<>(); // Default empty applications list
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<ApplicationObject> getApplications() {
        return applications;
    }

    public void setApplications(List<ApplicationObject> applications) {
        this.applications = applications;
    }
}

/**
 * Class representing an application tied to an account.
 */
class ApplicationObject {
    private int applicationId;
    private int roomId;
    private String status;
    private String applicationDate;
    private String decisionDate;

    // Default constructor
    public ApplicationObject() {
    }

    // Parameterized constructor
    public ApplicationObject(int applicationId, int roomId, String status, String applicationDate, String decisionDate) {
        this.applicationId = applicationId;
        this.roomId = roomId;
        this.status = status;
        this.applicationDate = applicationDate;
        this.decisionDate = decisionDate;
    }

    // Getters and Setters
    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(String decisionDate) {
        this.decisionDate = decisionDate;
    }
}
