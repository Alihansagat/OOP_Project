package university.ui;

import university.enums.*;
import university.exceptions.*;
import university.factory.UserFactory;
import university.models.*;
import university.service.AuthenticationService;
import university.service.ResearchService;
import university.storage.DataStorage;
import java.time.LocalDate;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final AuthenticationService authService = new AuthenticationService();
    private static final ResearchService researchService = new ResearchService();
    private static User currentUser = null;

    public static void main(String[] args) {
        seedData();
        System.out.println("  Welcome to University Information System ");

        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                showRoleMenu();
            }
        }
    }

    private static void seedData() {
        DataStorage db = DataStorage.getInstance();

        // Admin
        Admin admin = Admin.getInstance("A001", "Admin", "Admin", "admin@kbtu.kz", "admin123");
        db.addUser(admin);

        Admin admin1 = Admin.getInstance("A002", "Admin1", "Admin", "admin1@kbtu.kz", "admin123");
        db.addUser(admin1);

        // Teachers
        Teacher teacher1 = (Teacher) UserFactory.createUser(UserType.TEACHER, "Tomiris", "Abdildaeva", "Tomiris@kbtu.kz", "123");
        teacher1.setPosition(Position.PROFESSOR);
        Teacher teacher2 = (Teacher) UserFactory.createUser(UserType.TEACHER, "Asylzhan", "Izbasar", "Asyl@kbtu.kz", "123");
        db.addUser(teacher1);
        db.addUser(teacher2);

        // Manager
        Manager manager = new Manager("M001", "Alikhan", "Sagatov", "manager@kbtu.kz", "123", "EMP001", 250000, ManagerType.DEAN_OFFICE);
        db.addUser(manager);

        // Students
        Student student1 = new Student("U001", "Alikhan", "Sagat", "Alikhan@kbtu.kz", "123", "S001", "CS", 2, Degree.BACHELOR);
        Student student2 = new Student("U002", "Zhansaya", "Amanbay", "Zhansaya@kbtu.kz", "123", "S002", "CS", 2, Degree.BACHELOR);
        GraduateStudent gradStudent = new GraduateStudent("U003", "Marya", "Ganybek", "Marya@kbtu.kz", "123", "S003", "CS", 1, Degree.MASTER);
        db.addUser(student1);
        db.addUser(student2);
        db.addUser(gradStudent);

        // Tech support
        TechSupportSpecialist tech = new TechSupportSpecialist("T001", "Tom", "Tech", "tom@uni.edu", "pass123");
        db.addUser(tech);

        // Courses
        Course oop = new Course("OOP", 5, CourseType.MAJOR, "CS", 2);
        Course math = new Course("Calculus", 3, CourseType.MAJOR, "CS", 1);
        Course elective = new Course("Art History", 2, CourseType.FREE_ELECTIVE, "ALL", 1);
        oop.setOpen(true);
        math.setOpen(true);
        db.addCourse(oop);
        db.addCourse(math);
        db.addCourse(elective);
        manager.assignCourseToTeacher(oop, teacher1);
        manager.assignCourseToTeacher(math, teacher2);

        // Research paper for professor
        ResearcherDecorator professorResearcher = new ResearcherDecorator(teacher1);
        ResearchPaper paper1 = new ResearchPaper("10.1/abc", "Deep Learning Study", Arrays.asList("Bob Smith", "Carol Jones"), "IEEE", 12, LocalDate.of(2023, 5, 10));
        paper1.addCitation(); paper1.addCitation(); paper1.addCitation(); paper1.addCitation();

        ResearchPaper paper2 = new ResearchPaper("10.2/def", "Cloud Computing Advances", Arrays.asList("Bob Smith"), "ACM", 8, LocalDate.of(2022, 3, 15));
        paper2.addCitation(); paper2.addCitation(); paper2.addCitation();

        professorResearcher.addResearchPaper(paper1);
        professorResearcher.addResearchPaper(paper2);
        teacher1.assignResearcherRole(professorResearcher);
        db.addPaper(paper1);
        db.addPaper(paper2);

        // Journal
        Journal journal = new Journal("KBTU Research Journal", "SITE");
        db.addJournal(journal);

        // Grad student research
        ResearcherDecorator gradResearcher = new ResearcherDecorator(null) {
            @Override public String toString() { return "Madi Aslan (GraduateStudent)"; }
        };
        gradStudent.addResearchPaper(paper1);

        // News
        db.addNews(new News("Welcome!", "New semester has started.", "General"));
        db.addNews(new News("Research Conference", "Annual research conference upcoming.", "Research"));

        System.out.println("System initialized with sample data.\n");
    }

    private static void showLoginMenu() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        currentUser = authService.login(email, password);
        if (currentUser != null) {
            System.out.println("Logged in as: " + currentUser.getRole());
        }
    }

    private static void showRoleMenu() {
        switch (currentUser.getRole()) {
            case STUDENT  -> showStudentMenu();
            case TEACHER  -> showTeacherMenu();
            case MANAGER  -> showManagerMenu();
            case ADMIN    -> showAdminMenu();
            case TECH_SUPPORT -> showTechSupportMenu();
            default -> { System.out.println("Unknown role."); currentUser = null; }
        }
    }

    private static void showStudentMenu() {
        Student s = (Student) currentUser;
        System.out.println("\n=== STUDENT MENU: " + s.getFirstName() + " ===");
        System.out.println("1. View Dashboard   2. View Courses   3. Register for Course");
        System.out.println("4. Drop Course      5. View Marks     6. View Transcript");
        System.out.println("7. Rate Teacher     8. View News      9. Send Message");
        System.out.println("10. Switch Language 11. Submit Tech Request  0. Logout");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> s.displayDashboard();
            case "2" -> viewAllCourses();
            case "3" -> studentRegisterCourse(s);
            case "4" -> studentDropCourse(s);
            case "5" -> s.viewMarks();
            case "6" -> s.viewTranscript();
            case "7" -> studentRateTeacher(s);
            case "8" -> viewNews();
            case "9" -> sendMessage(s);
            case "10" -> switchLanguage(s);
            case "11" -> submitTechRequest(s);
            case "0" -> logout();
            default -> System.out.println("Invalid option.");
        }
    }

    private static void studentRegisterCourse(Student s) {
        viewAllCourses();
        System.out.print("Enter course ID: ");
        String id = scanner.nextLine().trim();
        Course c = DataStorage.getInstance().getCourse(id);
        if (c == null) { System.out.println("Course not found."); return; }
        try { s.registerForCourse(c); }
        catch (CourseRegistrationException e) { System.out.println("Error: " + e.getMessage()); }
    }

    private static void studentDropCourse(Student s) {
        if (s.getRegisteredCourses().isEmpty()) { System.out.println("No registered courses."); return; }
        s.getRegisteredCourses().forEach(c -> System.out.println(c.getCourseId() + " - " + c.getName()));
        System.out.print("Enter course ID to drop: ");
        String id = scanner.nextLine().trim();
        Course c = DataStorage.getInstance().getCourse(id);
        if (c != null) s.dropCourse(c);
        else System.out.println("Course not found.");
    }

    private static void studentRateTeacher(Student s) {
        List<Teacher> teachers = DataStorage.getInstance().getTeachers();
        teachers.forEach(t -> System.out.println(t.getUserId() + " - " + t.getFirstName() + " " + t.getLastName() + " [" + t.getPosition() + "]"));
        System.out.print("Enter teacher ID: ");
        String id = scanner.nextLine().trim();
        User u = DataStorage.getInstance().getUser(id);
        if (u instanceof Teacher) {
            System.out.print("Rating (1-10): ");
            try {
                double rating = Double.parseDouble(scanner.nextLine().trim());
                s.rateTeacher((Teacher) u, rating);
            } catch (NumberFormatException e) { System.out.println("Invalid rating."); }
        } else { System.out.println("Teacher not found."); }
    }


    private static void showTeacherMenu() {
        Teacher t = (Teacher) currentUser;
        System.out.println("\n=== TEACHER MENU: " + t.getFirstName() + " ===");
        System.out.println("1. Dashboard   2. View Courses   3. View Students in Course");
        System.out.println("4. Put Mark    5. Send Complaint  6. View News");
        System.out.println("7. Send Message  8. Switch Language  0. Logout");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> t.displayDashboard();
            case "2" -> t.viewCourses();
            case "3" -> teacherViewStudents(t);
            case "4" -> teacherPutMark(t);
            case "5" -> teacherSendComplaint(t);
            case "6" -> viewNews();
            case "7" -> sendMessage(t);
            case "8" -> switchLanguage(t);
            case "0" -> logout();
            default -> System.out.println("Invalid option.");
        }
    }

    private static void teacherViewStudents(Teacher t) {
        if (t.getCourses().isEmpty()) { System.out.println("No courses assigned."); return; }
        t.getCourses().forEach(c -> System.out.println(c.getCourseId() + " - " + c.getName()));
        System.out.print("Enter course ID: ");
        String id = scanner.nextLine().trim();
        Course c = DataStorage.getInstance().getCourse(id);
        if (c != null) t.viewStudents(c);
        else System.out.println("Course not found.");
    }

    private static void teacherPutMark(Teacher t) {
        if (t.getCourses().isEmpty()) { System.out.println("No courses assigned."); return; }
        t.getCourses().forEach(c -> System.out.println(c.getCourseId() + " - " + c.getName()));
        System.out.print("Course ID: ");
        String cid = scanner.nextLine().trim();
        Course course = DataStorage.getInstance().getCourse(cid);
        if (course == null) { System.out.println("Course not found."); return; }
        course.getEnrolledStudents().forEach(s ->
            System.out.println(s.getUserId() + " - " + s.getFirstName()));
        System.out.print("Student ID: ");
        String sid = scanner.nextLine().trim();
        User u = DataStorage.getInstance().getUser(sid);
        if (!(u instanceof Student)) { System.out.println("Student not found."); return; }
        try {
            System.out.print("1st Attestation (0-30): ");
            double a1 = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("2nd Attestation (0-30): ");
            double a2 = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Final Exam (0-40): ");
            double fe = Double.parseDouble(scanner.nextLine().trim());
            t.putMark((Student) u, course, a1, a2, fe);
        } catch (NumberFormatException e) { System.out.println("Invalid input."); }
    }

    private static void teacherSendComplaint(Teacher t) {
        List<Manager> managers = DataStorage.getInstance().getManagers();
        if (managers.isEmpty()) { System.out.println("No managers found."); return; }
        managers.forEach(m -> System.out.println(m.getUserId() + " - " + m.getFirstName()));
        System.out.print("Manager ID: ");
        String mid = scanner.nextLine().trim();
        User mu = DataStorage.getInstance().getUser(mid);
        if (!(mu instanceof Manager)) { System.out.println("Manager not found."); return; }
        List<Student> students = DataStorage.getInstance().getStudents();
        students.forEach(s -> System.out.println(s.getUserId() + " - " + s.getFirstName()));
        System.out.print("Student ID: ");
        String sid = scanner.nextLine().trim();
        User su = DataStorage.getInstance().getUser(sid);
        if (!(su instanceof Student)) { System.out.println("Student not found."); return; }
        System.out.print("Reason: ");
        String reason = scanner.nextLine().trim();
        System.out.println("Urgency: 1=LOW  2=MEDIUM  3=HIGH");
        String ul = scanner.nextLine().trim();
        UrgencyLevel urgency = switch (ul) {
            case "2" -> UrgencyLevel.MEDIUM;
            case "3" -> UrgencyLevel.HIGH;
            default -> UrgencyLevel.LOW;
        };
        t.sendComplaint((Manager) mu, (Student) su, reason, urgency);
    }


    private static void showManagerMenu() {
        Manager m = (Manager) currentUser;
        System.out.println("\n=== MANAGER MENU: " + m.getFirstName() + " ===");
        System.out.println("1. Dashboard          2. View Students by GPA");
        System.out.println("3. View Students A-Z  4. Generate Academic Report");
        System.out.println("5. View Complaints    6. Assign Teacher to Course");
        System.out.println("7. Add News           8. View Tech Requests");
        System.out.println("9. View News          10. Send Message   0. Logout");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();
        DataStorage db = DataStorage.getInstance();
        switch (choice) {
            case "1" -> m.displayDashboard();
            case "2" -> m.viewStudentsByGpa(db.getStudents());
            case "3" -> m.viewStudentsAlphabetically(db.getStudents());
            case "4" -> m.generateAcademicReport(db.getStudents());
            case "5" -> m.viewComplaints();
            case "6" -> managerAssignTeacher(m);
            case "7" -> managerAddNews(m);
            case "8" -> m.viewTechRequests(db.getTechRequests());
            case "9" -> viewNews();
            case "10" -> sendMessage(m);
            case "0" -> logout();
            default -> System.out.println("Invalid option.");
        }
    }

    private static void managerAssignTeacher(Manager m) {
        DataStorage db = DataStorage.getInstance();
        db.getTeachers().forEach(t ->
            System.out.println(t.getUserId() + " - " + t.getFirstName()));
        System.out.print("Teacher ID: ");
        User u = db.getUser(scanner.nextLine().trim());
        if (!(u instanceof Teacher)) { System.out.println("Not found."); return; }
        db.getAllCourses().forEach(c ->
            System.out.println(c.getCourseId() + " - " + c.getName()));
        System.out.print("Course ID: ");
        Course c = db.getCourse(scanner.nextLine().trim());
        if (c == null) { System.out.println("Not found."); return; }
        m.assignCourseToTeacher(c, (Teacher) u);
    }

    private static void managerAddNews(Manager m) {
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Content: ");
        String content = scanner.nextLine().trim();
        System.out.print("Topic (e.g. Research / General): ");
        String topic = scanner.nextLine().trim();
        News news = new News(title, content, topic);
        List<News> newsList = new ArrayList<>(DataStorage.getInstance().getAllNews());
        m.manageNews(newsList, "add", news);
        DataStorage.getInstance().addNews(news);
    }

    private static void showAdminMenu() {
        Admin a = (Admin) currentUser;
        System.out.println("\n=== ADMIN MENU ===");
        System.out.println("1. Dashboard   2. View All Users   3. Add User");
        System.out.println("4. Remove User  5. View Logs   6. View News   0. Logout");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> a.displayDashboard();
            case "2" -> DataStorage.getInstance().getAllUsers().forEach(System.out::println);
            case "3" -> adminAddUser(a);
            case "4" -> adminRemoveUser(a);
            case "5" -> a.viewLogs();
            case "6" -> viewNews();
            case "0" -> logout();
            default -> System.out.println("Invalid option.");
        }
    }

    private static void adminAddUser(Admin a) {
        System.out.println("Type: 1=Student 2=Teacher 3=Manager 4=TechSupport");
        String t = scanner.nextLine().trim();
        UserType type = switch (t) {
            case "1" -> UserType.STUDENT;
            case "2" -> UserType.TEACHER;
            case "3" -> UserType.MANAGER;
            default -> UserType.TECH_SUPPORT;
        };
        System.out.print("First name: "); String fn = scanner.nextLine().trim();
        System.out.print("Last name: ");  String ln = scanner.nextLine().trim();
        System.out.print("Email: ");      String em = scanner.nextLine().trim();
        System.out.print("Password: ");   String pw = scanner.nextLine().trim();
        User user = UserFactory.createUser(type, fn, ln, em, pw);
        a.addUser(user);
    }

    private static void adminRemoveUser(Admin a) {
        DataStorage.getInstance().getAllUsers().forEach(System.out::println);
        System.out.print("Enter user ID to remove: ");
        String id = scanner.nextLine().trim();
        a.removeUser(id);
    }

    private static void showTechSupportMenu() {
        TechSupportSpecialist ts = (TechSupportSpecialist) currentUser;
        System.out.println("\n=== TECH SUPPORT MENU: " + ts.getFirstName() + " ===");
        System.out.println("1. Dashboard   2. View New Requests   3. Accept Request");
        System.out.println("4. Reject Request   5. Mark Done   0. Logout");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> ts.displayDashboard();
            case "2" -> ts.viewNewRequests();
            case "3" -> techAction(ts, "accept");
            case "4" -> techAction(ts, "reject");
            case "5" -> techAction(ts, "done");
            case "0" -> logout();
            default -> System.out.println("Invalid option.");
        }
    }

    private static void techAction(TechSupportSpecialist ts, String action) {
        List<TechRequest> reqs = DataStorage.getInstance().getTechRequests();
        if (reqs.isEmpty()) { System.out.println("No requests."); return; }
        for (int i = 0; i < reqs.size(); i++)
            System.out.println((i+1) + ". " + reqs.get(i));
        System.out.print("Request number: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            TechRequest req = reqs.get(idx);
            ts.receiveRequest(req);
            switch (action) {
                case "accept" -> ts.acceptRequest(req);
                case "reject" -> ts.rejectRequest(req);
                case "done"   -> ts.markDone(req);
            }
        } catch (Exception e) { System.out.println("Invalid selection."); }
    }


    private static void viewAllCourses() {
        System.out.println("=== Available Courses ===");
        DataStorage.getInstance().getAllCourses().forEach(c ->
            System.out.println("  " + c.getCourseId() + " | " + c));
    }

    private static void viewNews() {
        System.out.println("=== News Feed ===");
        DataStorage.getInstance().getAllNews().forEach(n -> System.out.println("  " + n));
    }

    private static void sendMessage(User sender) {
        DataStorage.getInstance().getAllUsers().forEach(u -> {
            if (!u.getUserId().equals(sender.getUserId()))
                System.out.println(u.getUserId() + " - " + u.getFirstName() + " " + u.getLastName());
        });
        System.out.print("Recipient ID: ");
        String id = scanner.nextLine().trim();
        User recipient = DataStorage.getInstance().getUser(id);
        if (recipient == null) { System.out.println("User not found."); return; }
        System.out.print("Message: ");
        String content = scanner.nextLine().trim();
        sender.sendMessage(recipient, content);
    }

    private static void switchLanguage(User user) {
        System.out.println("1=KZ  2=EN  3=RU");
        String choice = scanner.nextLine().trim();
        Language lang = switch (choice) {
            case "1" -> Language.KZ;
            case "3" -> Language.RU;
            default -> Language.EN;
        };
        user.switchLanguage(lang);
    }

    private static void submitTechRequest(User user) {
        System.out.print("Describe your issue: ");
        String desc = scanner.nextLine().trim();
        TechRequest req = new TechRequest(user, desc);
        DataStorage.getInstance().addTechRequest(req);
        System.out.println("Tech request submitted. Status: " + req.getStatus());
    }

    private static void logout() {
        authService.logout(currentUser);
        currentUser = null;
    }
}
