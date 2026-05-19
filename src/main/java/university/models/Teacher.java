package university.models;

import university.enums.Position;
import university.enums.UrgencyLevel;
import university.enums.UserType;
import university.interfaces.Researcher;
import java.util.*;

public class Teacher extends Employee implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private static int complaintCounter = 1;

    private Position position;
    private List<Course> courses;
    private double rating;
    private int ratingCount;
    private Researcher researcherRole;

    public Teacher(String userId, String firstName, String lastName, String email, String passwordHash, String employeeId, double salary, Position position) {
        super(userId, firstName, lastName, email, passwordHash, employeeId, salary);
        this.position = position;
        this.courses = new ArrayList<>();
        this.rating = 0.0;
        this.ratingCount = 0;
    }

    public void putMark(Student student, Course course, double att1, double att2, double finalExam) {
        if (att1 < 0 || att1 > 30) {
            System.out.println("First attestation must be between 0 and 30.");
            return;
        }

        if (att2 < 0 || att2 > 30) {
            System.out.println("Second attestation must be between 0 and 30.");
            return;
        }

        if (finalExam < 0 || finalExam > 40) {
            System.out.println("Final exam must be between 0 and 40.");
            return;
        }

        double total = att1 + att2 + finalExam;

        if (total > 100) {
            System.out.println("Total grade cannot exceed 100.");
            return;
        }

        for (Mark m : student.getMarks()) {
            if (m.getCourse().equals(course)) {
                // Using the exact setter methods referenced in your Main.java logic
                m.setAttestation1(att1);
                m.setAttestation2(att2);
                m.setFinalExam(finalExam);

                // Triggers student logic to evaluate fail counts and recalculate the 4.0 scale GPA
                student.update(m);

                System.out.println("Mark set for " + student.getFirstName() + " in " + course.getName() + ": " + total);
                return;
            }
        }
        System.out.println("Student is not registered for this course.");
    }

    public void sendComplaint(Manager manager, Student student, String reason, UrgencyLevel urgency) {
        Complaint complaint = new Complaint("CMP-" + complaintCounter++, this, student, reason, urgency, java.time.LocalDateTime.now());
        manager.receiveComplaint(complaint);
        System.out.println("Complaint sent to manager with urgency: " + urgency);
    }

    public void viewCourses() {
        System.out.println("| Courses of " + getFirstName() + " |");
        if (courses.isEmpty()) { System.out.println("No courses assigned."); return; }
        for (Course c : courses) System.out.println("  " + c);
    }

    public void viewStudents(Course course) {
        System.out.println("| Students in " + course.getName() + " |");
        for (Student s : course.getEnrolledStudents()) {
            System.out.println("  " + s.getFirstName() + " " + s.getLastName() + " | GPA: " + s.getGpa());
        }
    }

    public void addRating(double r) {
        rating = (rating * ratingCount + r) / (++ratingCount);
    }

    public void assignResearcherRole(ResearcherDecorator decorator) {
        this.researcherRole = decorator;
        System.out.println(getFirstName() + " is now a Researcher.");
    }

    public boolean isResearcher() {
        return researcherRole != null || position == Position.PROFESSOR;
    }

    @Override
    public UserType getRole() { return UserType.TEACHER; }

    @Override
    public void displayDashboard() {
        System.out.println("=== Teacher Dashboard: " + getFirstName() + " ===");
        System.out.println("Position: " + position + " | Courses: " + courses.size());
        System.out.printf("Rating: %.2f%n", rating);
    }

    public void update() {
        System.out.println("Teacher profile updated.");
    }

    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }
    public List<Course> getCourses() { return courses; }
    public void addCourse(Course course) { courses.add(course); }
    public double getRating() { return rating; }
    public Researcher getResearcherRole() { return researcherRole; }
}