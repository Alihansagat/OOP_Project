package university.service;

import university.models.User;
import university.storage.DataStorage;

public class AuthenticationService {

    public User login(String email, String password) {
        User user = DataStorage.getInstance().getUserByEmail(email);
        if (user != null && user.login(password)) {
            System.out.println("Login successful: " + user.getFirstName());
            return user;
        }
        System.out.println("Invalid email or password.");
        return null;
    }

    public void logout(User user) {
        user.logout();
    }
}
