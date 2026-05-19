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
    private static final SwitchingLanguage lang = SwitchingLanguage.getInstance();
    private static User currentUser = null;

    public static void main(String[] args) {
        DataStorage db = DataStorage.getInstance();
        if (!db.loadFromDisk()) {
            System.out.println("Database file not found. Initializing default data...");
            seedData();
        }

        System.out.println("=== Welcome to University Information System ===");

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
        if (teacher1 != null) teacher1.setPosition(Position.PROFESSOR);
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
        if (teacher1 != null) {
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
            gradStudent.addResearchPaper(paper1);
        }

        // Journal
        // Journal journal = new Journal("KBTU Research Journal", "SITE");
        // db.addJournal(journal);

        // News
        db.addNews(new News("Welcome!", "New semester has started.", "General"));
        db.addNews(new News("Research Conference", "Annual research conference upcoming.", "Research"));

        System.out.println("[System]: Initialized with sample data successfully.\n");
    }

    private static void showLoginMenu() {
        System.out.println("\n--- " + lang.getText("menu.login", "LOGIN") + " ---");
        System.out.print(lang.getText("prompt.email", "Email") + ": ");
        String email = scanner.nextLine().trim();
        System.out.print(lang.getText("prompt.password", "Password") + ": ");
        String password = scanner.nextLine().trim();

        currentUser = authService.login(email, password);
        if (currentUser != null) {
            System.out.println(lang.getText("message.welcome", "Logged in successfully as: ") + currentUser.getRole());
        } else {
            System.out.println(lang.getText("error.credentials", "Invalid email or password."));
        }
    }

    private static void showRoleMenu() {
        switch (currentUser.getRole()) {
            case STUDENT -> showStudentMenu();
            case TEACHER -> showTeacherMenu();
            case MANAGER -> showManagerMenu();
            case ADMIN -> showAdminMenu();
            case TECH_SUPPORT -> showTechSupportMenu();
            default -> {
                System.out.println(lang.getText("error.unknown_role", "Unknown role. Logged out."));
                currentUser = null;
            }
        }
    }

    // =================================================================================
    // STUDENT ROLE MENUS & ROUTING
    // =================================================================================
    private static void showStudentMenu() {
        Student s = (Student) currentUser;
        System.out.println("\n=== " + lang.getText("student.menu.title", "STUDENT MENU") + ": " + s.getFirstName() + " ===");
        System.out.println("1. " + lang.getText("student.dashboard", "View Dashboard") + "   2. " + lang.getText("student.view_courses", "View Courses") + "   3. " + lang.getText("student.register_course", "Register for Course"));
        System.out.println("4. " + lang.getText("student.drop_course", "Drop Course") + "       5. " + lang.getText("student.marks", "View Marks") + "         6. " + lang.getText("student.transcript", "View Transcript"));
        System.out.println("7. " + lang.getText("student.rate_teacher", "Rate Teacher") + "    8. " + lang.getText("student.view_news", "View News") + "         9. " + lang.getText("student.send_message", "Send Message"));
        System.out.println("10. " + lang.getText("menu.change_language", "Switch Language") + " 11. " + lang.getText("student.tech_request", "Submit Tech Request") + " 12. View Inbox " + "  0. " + lang.getText("menu.logout", "Logout"));

        System.out.print(lang.getText("prompt.choice", "Choice") + ": ");
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
            case "12" -> s.viewInbox();
            case "0" -> logout();
            default -> System.out.println(lang.getText("error.invalid", "Invalid option."));
        }
    }

    private static void studentRegisterCourse(Student s) {
        viewAllCourses();
        System.out.print(lang.getText("prompt.course_id", "Enter course ID") + ": ");
        String id = scanner.nextLine().trim();
        Course c = DataStorage.getInstance().getCourse(id);
        if (c == null) {
            System.out.println(lang.getText("error.course_not_found", "Course not found."));
            return;
        }
        try {
            s.registerForCourse(c);
            System.out.println(lang.getText("message.registered_success", "Successfully registered for course!"));
        } catch (CourseRegistrationException e) {
            System.out.println(lang.getText("error.prefix", "Error") + ": " + e.getMessage());
        }
    }

    private static void studentDropCourse(Student s) {
        if (s.getRegisteredCourses().isEmpty()) {
            System.out.println(lang.getText("student.no_courses", "No registered courses found."));
            return;
        }
        s.getRegisteredCourses().forEach(c -> System.out.println(c.getCourseId() + " - " + c.getName()));
        System.out.print(lang.getText("prompt.drop_course_id", "Enter course ID to drop") + ": ");
        String id = scanner.nextLine().trim();
        Course c = DataStorage.getInstance().getCourse(id);
        if (c != null) {
            s.dropCourse(c);
            System.out.println(lang.getText("message.drop_success", "Course dropped successfully."));
        } else {
            System.out.println(lang.getText("error.course_not_found", "Course not found."));
        }
    }

    private static void studentRateTeacher(Student s) {
        List<Teacher> teachers = DataStorage.getInstance().getTeachers();
        teachers.forEach(t -> System.out.println(t.getUserId() + " - " + t.getFirstName() + " " + t.getLastName() + " [" + t.getPosition() + "]"));
        System.out.print(lang.getText("prompt.teacher_id", "Enter teacher ID") + ": ");
        String id = scanner.nextLine().trim();
        User u = DataStorage.getInstance().getUser(id);
        if (u instanceof Teacher) {
            System.out.print(lang.getText("prompt.rating", "Rating (1-10)") + ": ");
            try {
                double rating = Double.parseDouble(scanner.nextLine().trim());
                s.rateTeacher((Teacher) u, rating);
                System.out.println(lang.getText("message.rating_success", "Thank you for rating your instructor."));
            } catch (NumberFormatException e) {
                System.out.println(lang.getText("error.invalid_rating", "Invalid rating format."));
            }
        } else {
            System.out.println(lang.getText("error.teacher_not_found", "Teacher not found."));
        }
    }

    // =================================================================================
    // TEACHER ROLE MENUS & ROUTING
    // =================================================================================
    private static void showTeacherMenu() {
        Teacher t = (Teacher) currentUser;
        System.out.println("\n=== " + lang.getText("teacher.menu.title", "TEACHER MENU") + ": " + t.getFirstName() + " ===");
        System.out.println("1. " + lang.getText("teacher.dashboard", "Dashboard") + "   2. " + lang.getText("teacher.view_courses", "View Courses") + "   3. " + lang.getText("teacher.view_students", "View Students in Course"));
        System.out.println("4. " + lang.getText("teacher.put_mark", "Put Mark") + "   5. " + lang.getText("teacher.complaint", "Send Complaint") + "  6. " + lang.getText("teacher.view_news", "View News"));
        System.out.println("7. " + lang.getText("teacher.send_message", "Send Message") + "  8. " + lang.getText("menu.change_language", "Switch Language") + "   0. " + lang.getText("menu.logout", "Logout"));

        System.out.print(lang.getText("prompt.choice", "Choice") + ": ");
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
            default -> System.out.println(lang.getText("error.invalid", "Invalid option."));
        }
    }

    private static void teacherViewStudents(Teacher t) {
        if (t.getCourses().isEmpty()) {
            System.out.println(lang.getText("teacher.no_courses", "No courses assigned."));
            return;
        }
        t.getCourses().forEach(c -> System.out.println(c.getCourseId() + " - " + c.getName()));
        System.out.print(lang.getText("prompt.course_id", "Enter course ID") + ": ");
        String id = scanner.nextLine().trim();
        Course c = DataStorage.getInstance().getCourse(id);
        if (c != null) t.viewStudents(c);
        else System.out.println(lang.getText("error.course_not_found", "Course not found."));
    }

    private static void teacherPutMark(Teacher t) {
        if (t.getCourses().isEmpty()) {
            System.out.println(lang.getText("teacher.no_courses", "No courses assigned."));
            return;
        }
        t.getCourses().forEach(c -> System.out.println(c.getCourseId() + " - " + c.getName()));
        System.out.print(lang.getText("prompt.course_id", "Enter course ID") + ": ");
        String cid = scanner.nextLine().trim();
        Course course = DataStorage.getInstance().getCourse(cid);
        if (course == null) {
            System.out.println(lang.getText("error.course_not_found", "Course not found."));
            return;
        }
        course.getEnrolledStudents().forEach(s -> System.out.println(s.getUserId() + " - " + s.getFirstName()));
        System.out.print(lang.getText("prompt.student_id", "Student ID") + ": ");
        String sid = scanner.nextLine().trim();
        User u = DataStorage.getInstance().getUser(sid);
        if (!(u instanceof Student)) {
            System.out.println(lang.getText("error.student_not_found", "Student not found."));
            return;
        }
        try {
            System.out.print(lang.getText("teacher.mark.a1", "1st Attestation (0-30)") + ": ");
            double a1 = Double.parseDouble(scanner.nextLine().trim());
            System.out.print(lang.getText("teacher.mark.a2", "2nd Attestation (0-30)") + ": ");
            double a2 = Double.parseDouble(scanner.nextLine().trim());
            System.out.print(lang.getText("teacher.mark.final", "Final Exam (0-40)") + ": ");
            double fe = Double.parseDouble(scanner.nextLine().trim());
            t.putMark((Student) u, course, a1, a2, fe);
            System.out.println(lang.getText("message.mark_success", "Marks successfully published."));
        } catch (NumberFormatException e) {
            System.out.println(lang.getText("error.invalid", "Invalid input."));
        }
    }

    private static void teacherSendComplaint(Teacher t) {
        List<Manager> managers = DataStorage.getInstance().getManagers();
        if (managers.isEmpty()) {
            System.out.println(lang.getText("manager.none_found", "No managers found."));
            return;
        }
        managers.forEach(m -> System.out.println(m.getUserId() + " - " + m.getFirstName()));
        System.out.print(lang.getText("prompt.manager_id", "Manager ID") + ": ");
        String mid = scanner.nextLine().trim();
        User mu = DataStorage.getInstance().getUser(mid);
        if (!(mu instanceof Manager)) {
            System.out.println(lang.getText("error.manager_not_found", "Manager not found."));
            return;
        }

        List<Student> students = DataStorage.getInstance().getStudents();
        students.forEach(s -> System.out.println(s.getUserId() + " - " + s.getFirstName()));
        System.out.print(lang.getText("prompt.student_id", "Student ID") + ": ");
        String sid = scanner.nextLine().trim();
        User su = DataStorage.getInstance().getUser(sid);
        if (!(su instanceof Student)) {
            System.out.println(lang.getText("error.student_not_found", "Student not found."));
            return;
        }

        System.out.print(lang.getText("prompt.reason", "Reason") + ": ");
        String reason = scanner.nextLine().trim();
        System.out.println(lang.getText("prompt.urgency", "Urgency: 1=LOW  2=MEDIUM  3=HIGH"));
        String ul = scanner.nextLine().trim();
        UrgencyLevel urgency = switch (ul) {
            case "2" -> UrgencyLevel.MEDIUM;
            case "3" -> UrgencyLevel.HIGH;
            default -> UrgencyLevel.LOW;
        };
        t.sendComplaint((Manager) mu, (Student) su, reason, urgency);
        System.out.println(lang.getText("message.complaint_sent", "Complaint filed successfully."));
    }

    // =================================================================================
    // MANAGER ROLE MENUS & ROUTING
    // =================================================================================
    private static void showManagerMenu() {
        Manager m = (Manager) currentUser;
        System.out.println("\n=== " + lang.getText("manager.menu.title", "MANAGER MENU") + ": " + m.getFirstName() + " ===");
        System.out.println("1. " + lang.getText("manager.dashboard", "Dashboard") + "          2. " + lang.getText("manager.view_gpa", "View Students by GPA"));
        System.out.println("3. " + lang.getText("manager.view_alpha", "View Students A-Z") + "  4. " + lang.getText("manager.report", "Generate Academic Report"));
        System.out.println("5. " + lang.getText("manager.complaints", "View Complaints") + "    6. " + lang.getText("manager.assign_course", "Assign Teacher to Course"));
        System.out.println("7. " + lang.getText("manager.add_news", "Add News") + "           8. " + lang.getText("manager.tech_requests", "View Tech Requests"));
        System.out.println("9. " + lang.getText("manager.view_news", "View News") + "          10. " + lang.getText("manager.send_message", "Send Message") + "   0. " + lang.getText("menu.logout", "Logout"));

        System.out.print(lang.getText("prompt.choice", "Choice") + ": ");
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
            default -> System.out.println(lang.getText("error.invalid", "Invalid option."));
        }
    }

    private static void managerAssignTeacher(Manager m) {
        DataStorage db = DataStorage.getInstance();
        db.getTeachers().forEach(t -> System.out.println(t.getUserId() + " - " + t.getFirstName()));
        System.out.print(lang.getText("prompt.teacher_id", "Teacher ID") + ": ");
        User u = db.getUser(scanner.nextLine().trim());
        if (!(u instanceof Teacher)) {
            System.out.println(lang.getText("error.teacher_not_found", "Teacher not found."));
            return;
        }
        db.getAllCourses().forEach(c -> System.out.println(c.getCourseId() + " - " + c.getName()));
        System.out.print(lang.getText("prompt.course_id", "Course ID") + ": ");
        Course c = db.getCourse(scanner.nextLine().trim());
        if (c == null) {
            System.out.println(lang.getText("error.course_not_found", "Course not found."));
            return;
        }
        m.assignCourseToTeacher(c, (Teacher) u);
        System.out.println(lang.getText("message.assigned_success", "Instructor assigned to course successfully."));
    }

    private static void managerAddNews(Manager m) {
        System.out.print(lang.getText("news.title", "Title") + ": ");
        String title = scanner.nextLine().trim();
        System.out.print(lang.getText("news.content", "Content") + ": ");
        String content = scanner.nextLine().trim();
        System.out.print(lang.getText("news.topic", "Topic (e.g. Research / General)") + ": ");
        String topic = scanner.nextLine().trim();
        News news = new News(title, content, topic);
        List<News> newsList = new ArrayList<>(DataStorage.getInstance().getAllNews());
        m.manageNews(newsList, "add", news);
        DataStorage.getInstance().addNews(news);
        System.out.println(lang.getText("message.news_added", "News post published successfully."));
    }

    // =================================================================================
    // ADMIN ROLE MENUS & ROUTING
    // =================================================================================
    private static void showAdminMenu() {
        Admin a = (Admin) currentUser;
        System.out.println("\n=== " + lang.getText("admin.menu.title", "ADMIN MENU") + " ===");
        System.out.println("1. " + lang.getText("admin.dashboard", "Dashboard") + "   2. " + lang.getText("admin.view_users", "View All Users") + "   3. " + lang.getText("admin.add_user", "Add User"));
        System.out.println("4. " + lang.getText("admin.remove_user", "Remove User") + "  5. " + lang.getText("admin.logs", "View Logs") + "   6. " + lang.getText("admin.view_news", "View News") + "   0. " + lang.getText("menu.logout", "Logout"));

        System.out.print(lang.getText("prompt.choice", "Choice") + ": ");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> a.displayDashboard();
            case "2" -> DataStorage.getInstance().getAllUsers().forEach(System.out::println);
            case "3" -> adminAddUser(a);
            case "4" -> adminRemoveUser(a);
            case "5" -> a.viewLogs();
            case "6" -> viewNews();
            case "0" -> logout();
            default -> System.out.println(lang.getText("error.invalid", "Invalid option."));
        }
    }

    private static void adminAddUser(Admin a) {
        System.out.println(lang.getText("admin.type_prompt", "Type: 1=Student 2=Teacher 3=Manager 4=TechSupport"));
        String t = scanner.nextLine().trim();
        UserType type = switch (t) {
            case "1" -> UserType.STUDENT;
            case "2" -> UserType.TEACHER;
            case "3" -> UserType.MANAGER;
            default -> UserType.TECH_SUPPORT;
        };
        System.out.print(lang.getText("prompt.first_name", "First name") + ": "); String fn = scanner.nextLine().trim();
        System.out.print(lang.getText("prompt.last_name", "Last name") + ": ");  String ln = scanner.nextLine().trim();
        System.out.print(lang.getText("prompt.email", "Email") + ": ");      String em = scanner.nextLine().trim();
        System.out.print(lang.getText("prompt.password", "Password") + ": ");   String pw = scanner.nextLine().trim();

        User user = UserFactory.createUser(type, fn, ln, em, pw);
        if (user != null) {
            a.addUser(user);
            System.out.println(lang.getText("message.registered", "User created and stored contextually."));
        }
    }

    private static void adminRemoveUser(Admin a) {
        DataStorage.getInstance().getAllUsers().forEach(System.out::println);
        System.out.print(lang.getText("prompt.remove_id", "Enter user ID to remove") + ": ");
        String id = scanner.nextLine().trim();
        a.removeUser(id);
        System.out.println(lang.getText("message.remove_success", "User profile cleared."));
    }

    // =================================================================================
    // TECH SUPPORT ROLE MENUS & ROUTING
    // =================================================================================
    private static void showTechSupportMenu() {
        TechSupportSpecialist ts = (TechSupportSpecialist) currentUser;
        System.out.println("\n=== " + lang.getText("tech.menu.title", "TECH SUPPORT MENU") + ": " + ts.getFirstName() + " ===");
        System.out.println("1. " + lang.getText("tech.dashboard", "Dashboard") + "   2. " + lang.getText("tech.new_requests", "View New Requests") + "   3. " + lang.getText("tech.accept", "Accept Request"));
        System.out.println("4. " + lang.getText("tech.reject", "Reject Request") + "   5. " + lang.getText("tech.done", "Mark Done") + "   0. " + lang.getText("menu.logout", "Logout"));

        System.out.print(lang.getText("prompt.choice", "Choice") + ": ");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> ts.displayDashboard();
            case "2" -> ts.viewNewRequests();
            case "3" -> techAction(ts, "accept");
            case "4" -> techAction(ts, "reject");
            case "5" -> techAction(ts, "done");
            case "0" -> logout();
            default -> System.out.println(lang.getText("error.invalid", "Invalid option."));
        }
    }

    private static void techAction(TechSupportSpecialist ts, String action) {
        List<TechRequest> reqs = DataStorage.getInstance().getTechRequests();
        if (reqs.isEmpty()) {
            System.out.println(lang.getText("tech.no_requests", "No active support tickets."));
            return;
        }
        for (int i = 0; i < reqs.size(); i++) {
            System.out.println((i + 1) + ". " + reqs.get(i));
        }
        System.out.print(lang.getText("prompt.ticket_number", "Request ticket number") + ": ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            TechRequest req = reqs.get(idx);
            ts.receiveRequest(req);
            switch (action) {
                case "accept" -> ts.acceptRequest(req);
                case "reject" -> ts.rejectRequest(req);
                case "done"   -> ts.markDone(req);
            }
            System.out.println(lang.getText("message.ticket_updated", "Ticket state adjusted successfully."));
        } catch (Exception e) {
            System.out.println(lang.getText("error.invalid", "Invalid selection index."));
        }
    }

    // =================================================================================
    // GLOBAL HELPERS & UTILITIES
    // =================================================================================
    private static void viewAllCourses() {
        System.out.println("=== " + lang.getText("course.available_title", "Available Courses") + " ===");
        DataStorage.getInstance().getAllCourses().forEach(c ->
                System.out.println("  " + c.getCourseId() + " | " + c));
    }

    private static void viewNews() {
        System.out.println("=== " + lang.getText("news.feed_title", "News Feed") + " ===");
        DataStorage.getInstance().getAllNews().forEach(n -> System.out.println("  " + n));
    }

    private static void sendMessage(User sender) {
        DataStorage.getInstance().getAllUsers().forEach(u -> {
            if (!u.getUserId().equals(sender.getUserId()))
                System.out.println(u.getUserId() + " - " + u.getFirstName() + " " + u.getLastName());
        });
        System.out.print(lang.getText("prompt.recipient_id", "Recipient ID") + ": ");
        String id = scanner.nextLine().trim();
        User recipient = DataStorage.getInstance().getUser(id);
        if (recipient == null) {
            System.out.println(lang.getText("error.user_not_found", "Recipient profile not found."));
            return;
        }
        System.out.print(lang.getText("prompt.message_body", "Message Body") + ": ");
        String content = scanner.nextLine().trim();
        sender.sendMessage(recipient, content);
        System.out.println(lang.getText("message.sent_success", "Message transmitted successfully."));
    }

    private static void switchLanguage(User user) {
        System.out.println("1=KZ   2=EN   3=RU");
        System.out.print(lang.getText("prompt.select", "Select language preference") + ": ");
        String choice = scanner.nextLine().trim();
        Language selectedLanguage = switch (choice) {
            case "1" -> Language.KZ;
            case "3" -> Language.RU;
            default -> Language.EN;
        };

        // Synchronizes language changes globally with SwitchingLanguage singleton
        lang.setLanguage(selectedLanguage);
        user.switchLanguage(selectedLanguage);
        System.out.println(lang.getText("message.lang_changed", "System localization environment updated successfully."));
    }

    private static void submitTechRequest(User user) {
        System.out.print(lang.getText("prompt.issue_desc", "Describe your hardware/software issue") + ": ");
        String desc = scanner.nextLine().trim();
        TechRequest req = new TechRequest(user, desc);
        DataStorage.getInstance().addTechRequest(req);
        System.out.println(lang.getText("message.tech_submitted", "Tech ticket recorded. Current Status") + ": " + req.getStatus());
    }

    private static void logout() {
        if (currentUser != null) {
            authService.logout(currentUser);

            // Автоматически сохраняем измененные данные на диск при выходе пользователя
            DataStorage.getInstance().saveToDisk();

            System.out.println("Сессия закрыта. Данные сохранены.");
            currentUser = null;
        }
    }
}