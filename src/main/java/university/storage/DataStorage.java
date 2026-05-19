package university.storage;

import university.models.*;
import java.io.*;
import java.util.*;

public class DataStorage implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String FILE_PATH = "university_data.ser";
    private static DataStorage instance;

    // Initialize collections directly to avoid NullPointerExceptions on a clean start
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Course> courses = new HashMap<>();
    private final Map<String, ResearchPaper> papers = new HashMap<>();
    private final Map<String, News> newsList = new LinkedHashMap<>(); // Use LinkedHashMap to preserve sort order
    private final List<TechRequest> techRequests = new ArrayList<>();
    private final List<Journal> journals = new ArrayList<>();
    private final List<Complaint> complaints = new ArrayList<>();

    // Keep constructor private for Singleton pattern
    private DataStorage() {}

    // Global access point to get the single storage instance
    public static synchronized DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    // Dump the current state of this DataStorage object into a local file
    public synchronized void saveToDisk() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(this);
            System.out.println("[DataStorage]: Data successfully saved to file.");
        } catch (IOException e) {
            System.err.println("[DataStorage Error]: Could not save data: " + e.getMessage());
        }
    }

    // Read the DataStorage object from the file if it exists
    public static synchronized boolean loadFromDisk() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return false; // No file found, means it's the first time running the app
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            instance = (DataStorage) ois.readObject();
            System.out.println("[DataStorage]: Data successfully loaded from file.");
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[DataStorage Error]: Error reading data file: " + e.getMessage());
            return false;
        }
    }

    // User management methods
    public void addUser(User user) {
        if (user != null && user.getUserId() != null) {
            users.put(user.getUserId(), user);
        }
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public User getUserByEmail(String email) {
        if (email == null) return null;
        return users.values().stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst()
                .orElse(null);
    }

    public void removeUser(String userId) {
        users.remove(userId);
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }

    // Filter the user map to return only student objects
    public List<Student> getStudents() {
        List<Student> result = new ArrayList<>();
        for (User u : users.values()) {
            if (u instanceof Student) result.add((Student) u);
        }
        return result;
    }

    // Filter the user map to return only teacher objects
    public List<Teacher> getTeachers() {
        List<Teacher> result = new ArrayList<>();
        for (User u : users.values()) {
            if (u instanceof Teacher) result.add((Teacher) u);
        }
        return result;
    }

    // Filter the user map to return only manager objects
    public List<Manager> getManagers() {
        List<Manager> result = new ArrayList<>();
        for (User u : users.values()) {
            if (u instanceof Manager) result.add((Manager) u);
        }
        return result;
    }

    // Course management methods
    public void addCourse(Course course) {
        if (course != null && course.getCourseId() != null) {
            courses.put(course.getCourseId(), course);
        }
    }

    public Course getCourse(String id) {
        return courses.get(id);
    }

    public Collection<Course> getAllCourses() {
        return courses.values();
    }

    public void removeCourse(String id) {
        courses.remove(id);
    }

    // Research paper management methods
    public void addPaper(ResearchPaper paper) {
        if (paper != null && paper.getDoi() != null) {
            papers.put(paper.getDoi(), paper);
        }
    }

    public Collection<ResearchPaper> getAllPapers() {
        return papers.values();
    }

    // News feed management methods
    public void addNews(News news) {
        if (news == null || news.getNewsId() == null) return;

        newsList.put(news.getNewsId(), news);

        // Sort the news feed contextually
        List<News> sorted = new ArrayList<>(newsList.values());
        Collections.sort(sorted);

        newsList.clear();
        for (News n : sorted) {
            newsList.put(n.getNewsId(), n);
        }
    }

    public Collection<News> getAllNews() {
        return newsList.values();
    }

    public void removeNews(String id) {
        newsList.remove(id);
    }

    // Support requests, journals, and complaints
    public void addTechRequest(TechRequest req) {
        if (req != null) techRequests.add(req);
    }

    public List<TechRequest> getTechRequests() {
        return techRequests;
    }

    public void addJournal(Journal j) {
        if (j != null) journals.add(j);
    }

    public List<Journal> getJournals() {
        return journals;
    }

    public void addComplaint(Complaint c) {
        if (c != null) complaints.add(c);
    }

    public List<Complaint> getComplaints() {
        return complaints;
    }
}