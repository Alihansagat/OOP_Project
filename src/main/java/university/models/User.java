package university.models;

import university.enums.Language;
import university.enums.UserType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class User implements Serializable {
    private static int nextId = 1;

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    private Language language;
    protected List<Message> inbox;

    public User(String userId, String firstName, String lastName, String email, String passwordHash) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.language = Language.EN;
        this.inbox = new ArrayList<>();
    }

    public User() {
        this.inbox = new ArrayList<>();
        this.language = Language.EN;
    }

    public boolean login(String password) {
        return this.passwordHash.equals(password);
    }

    public void logout() {
        System.out.println(firstName + " " + lastName + " logged out.");
    }

    public void switchLanguage(Language lang) {
        this.language = lang;
        System.out.println("Language switched to: " + lang);
    }

    public abstract UserType getRole();
    public abstract void displayDashboard();

    public void sendMessage(User recipient, String content) {
        Message msg = new Message("M" + nextId++, this, recipient, content, java.time.LocalDateTime.now());
        recipient.inbox.add(msg);
        System.out.println("Message sent to " + recipient.getFirstName());
    }

    public void viewInbox() {
        if (inbox.isEmpty()) {
            System.out.println("No messages.");
            return;
        }
        System.out.println("| Inbox |");
        for (Message m : inbox) {
            System.out.println(m);
        }
    }

    // Getters and setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Language getLanguage() { return language; }
    public void setLanguage(Language language) { this.language = language; }

    public List<Message> getInbox() { return inbox; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return getRole() + " | " + firstName + " " + lastName + " | " + email;
    }

    public int compareTo(User other) {
        return this.lastName.compareTo(other.lastName);
    }
}
