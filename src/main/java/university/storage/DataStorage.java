package university.storage;

import university.models.*;
import java.io.*;
import java.util.*;

public class DataStorage implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String FILE_PATH = "data.ser";
    private static DataStorage instance;

    private Map<String, User> users;
    private Map<String, Course> courses;
    private Map<String, ResearchPaper> papers;
    private Map<String, News> newsList;
    private List<TechRequest> techRequests;
    private List<Journal> journals;
    private List<Complaint> complaints;

    private DataStorage() {
        users = new HashMap<>();
        courses = new HashMap<>();
        papers = new HashMap<>();
        newsList = new LinkedHashMap<>();
        techRequests = new ArrayList<>();
        journals = new ArrayList<>();
        complaints = new ArrayList<>();
    }

    public static DataStorage getInstance() {
        if (instance == null) {
            instance = loadFromFile();
            if (instance == null) instance = new DataStorage();
        }
        return instance;
    }

    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public User getUserByEmail(String email) {
        return users.values().stream()
            .filter(u -> u.getEmail().equals(email))
            .findFirst().orElse(null);
    }

    public void removeUser(String userId) {
        users.remove(userId);
    }

    public Collection<User> getAllUsers() { return users.values(); }

    public List<Student> getStudents() {
        List<Student> result = new ArrayList<>();
        for (User u : users.values())
            if (u instanceof Student) result.add((Student) u);
        return result;
    }

    public List<Teacher> getTeachers() {
        List<Teacher> result = new ArrayList<>();
        for (User u : users.values())
            if (u instanceof Teacher) result.add((Teacher) u);
        return result;
    }

    public List<Manager> getManagers() {
        List<Manager> result = new ArrayList<>();
        for (User u : users.values())
            if (u instanceof Manager) result.add((Manager) u);
        return result;
    }

    public void addCourse(Course course) { courses.put(course.getCourseId(), course); }
    public Course getCourse(String id) { return courses.get(id); }
    public Collection<Course> getAllCourses() { return courses.values(); }
    public void removeCourse(String id) { courses.remove(id); }

    public void addPaper(ResearchPaper paper) { papers.put(paper.getDoi(), paper); }
    public Collection<ResearchPaper> getAllPapers() { return papers.values(); }

    public void addNews(News news) {
        newsList.put(news.getNewsId(), news);
        List<News> sorted = new ArrayList<>(newsList.values());
        Collections.sort(sorted);
        newsList.clear();
        for (News n : sorted) newsList.put(n.getNewsId(), n);
    }
    public Collection<News> getAllNews() { return newsList.values(); }
    public void removeNews(String id) { newsList.remove(id); }

    public void addTechRequest(TechRequest req) { techRequests.add(req); }
    public List<TechRequest> getTechRequests() { return techRequests; }

    public void addJournal(Journal j) { journals.add(j); }
    public List<Journal> getJournals() { return journals; }

    public void addComplaint(Complaint c) { complaints.add(c); }
    public List<Complaint> getComplaints() { return complaints; }

    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(this);
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private static DataStorage loadFromFile() {
        File f = new File(FILE_PATH);
        if (!f.exists()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            DataStorage loaded = (DataStorage) ois.readObject();
            System.out.println("Data loaded successfully.");
            return loaded;
        } catch (Exception e) {
            System.out.println("Could not load data, starting fresh.");
            return null;
        }
    }
}
