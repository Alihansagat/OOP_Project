package university.models;

import university.enums.UserType;
import university.storage.DataStorage;
import java.util.*;

public class Admin extends User {
    private static Admin instance;

    private List<String> actionLog;

    private Admin(String userId, String firstName, String lastName, String email, String passwordHash) {
        super(userId, firstName, lastName, email, passwordHash);
        this.actionLog = new ArrayList<>();
    }

    public static Admin getInstance(String userId, String firstName, String lastName, String email, String passwordHash) {
        if (instance == null) {
            instance = new Admin(userId, firstName, lastName, email, passwordHash);
        }
        return instance;
    }

    public static Admin getInstance() {
        return instance;
    }

    public void addUser(User user) {
        DataStorage.getInstance().addUser(user);
        log("Added user: " + user.getFirstName() + " " + user.getLastName());
    }

    public void removeUser(String userId) {
        User user = DataStorage.getInstance().getUser(userId);
        if (user != null) {
            DataStorage.getInstance().removeUser(userId);
            log("Removed user: " + userId);
        } else {
            System.out.println("User not found: " + userId);
        }
    }

    public void updateUser(User user) {
        DataStorage.getInstance().addUser(user);
        log("Updated user: " + user.getUserId());
    }

    public void viewLogs() {
        System.out.println("| Action Log |");
        for (String l : actionLog) {
            System.out.println("  " + l);
        }
    }

    private void log(String action) {
        String entry = java.time.LocalDateTime.now() + " | " + action;
        actionLog.add(entry);
        System.out.println("[LOG] " + entry);
    }

    @Override
    public UserType getRole() { return UserType.ADMIN; }

    @Override
    public void displayDashboard() {
        System.out.println("=== Admin Dashboard ===");
        System.out.println("Total users: " + DataStorage.getInstance().getAllUsers().size());
        System.out.println("Log entries: " + actionLog.size());
    }

    public List<String> getActionLog() { return actionLog; }
}
