package university.factory;

import university.enums.*;
import university.models.*;

public class UserFactory {
    private static int nextId = 1;

    public static User createUser(UserType type, String firstName, String lastName, String email, String password) {
        String id = String.valueOf(nextId++);
        User user;

        switch (type) {
            case STUDENT:
                user = new Student(id, firstName, lastName, email, password, "S" + id, "CS", 1, Degree.BACHELOR);
                break;
            case TEACHER:
                user = new Teacher(id, firstName, lastName, email, password, "T" + id, 150000, Position.LECTURER);
                break;
            case MANAGER:
                user = new Manager(id, firstName, lastName, email, password, "M" + id, 200000, ManagerType.DEPARTMENT);
                break;
            case ADMIN:
                user = Admin.getInstance(id, firstName, lastName, email, password);
                break;
            case TECH_SUPPORT:
                user = new TechSupportSpecialist(id, firstName, lastName, email, password);
                break;
            default:
                throw new IllegalArgumentException("Unknown user type: " + type);
        }

        return user;
    }
}