package university.models;

import university.enums.Degree;
import university.enums.UserType;
import university.exceptions.CourseRegistrationException;
import university.exceptions.NonResearcherException;
import university.interfaces.Researcher;
import java.util.*;

public class Student extends User implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String studentId;
    private String major;
    private int yearOfStudy;
    private double gpa;
    private int totalCredits;
    private int failCount;
    private List<Course> registeredCourses;
    private List<Mark> marks;
    private List<String> organizations;
    private boolean isOrganizationHead;
    private Degree degree;
    private Researcher researcherRole;

    private static final int MAX_CREDITS = 21;
    private static final int MAX_FAILS = 3;

    public Student(String userId, String firstName, String lastName, String email, String passwordHash, String studentId, String major, int yearOfStudy, Degree degree) {
        super(userId, firstName, lastName, email, passwordHash);
        this.studentId = studentId;
        this.major = major;
        this.yearOfStudy = yearOfStudy;
        this.degree = degree;
        this.gpa = 0.0;
        this.totalCredits = MAX_CREDITS;
        this.failCount = 0;
        this.registeredCourses = new ArrayList<>();
        this.marks = new ArrayList<>();
        this.organizations = new ArrayList<>();
        this.isOrganizationHead = false;
    }

    // Helper method to convert raw points out of 100 to the 4.0 GPA scale
    private double getGradePoints(double totalMarks) {
        if (totalMarks >= 95) return 4.00; // A
        if (totalMarks >= 90) return 3.67; // A-
        if (totalMarks >= 85) return 3.33; // B+
        if (totalMarks >= 80) return 3.00; // B
        if (totalMarks >= 75) return 2.67; // B-
        if (totalMarks >= 70) return 2.33; // C+
        if (totalMarks >= 65) return 2.00; // C
        if (totalMarks >= 60) return 1.67; // C-
        if (totalMarks >= 55) return 1.33; // D+
        if (totalMarks >= 50) return 1.00; // D
        return 0.00;                       // F
    }

    public void registerForCourse(Course course) throws CourseRegistrationException {
        if (!course.isOpen()) {
            throw new CourseRegistrationException("Registration is closed for: " + course.getName());
        }
        if (totalCredits - course.getCredits() < 0) {
            throw new CourseRegistrationException(
                    "Not enough credits. Max " + MAX_CREDITS + ", need " + course.getCredits());
        }
        if (registeredCourses.contains(course)) {
            throw new CourseRegistrationException("Already registered for: " + course.getName());
        }
        registeredCourses.add(course);
        course.enrollStudent(this);
        totalCredits -= course.getCredits();
        marks.add(new Mark(this, course));
        System.out.println("Registered for: " + course.getName());
    }

    public void dropCourse(Course course) {
        if (registeredCourses.remove(course)) {
            course.removeStudent(this);
            totalCredits += course.getCredits();
            marks.removeIf(m -> m.getCourse().equals(course));
            updateGpa(); // Recalculate GPA when dropping courses
            System.out.println("Dropped: " + course.getName());
        } else {
            System.out.println("Not registered for: " + course.getName());
        }
    }

    public void viewMarks() {
        System.out.println("| Marks for " + getFirstName() + " " + getLastName() + " |");
        if (marks.isEmpty()) { System.out.println("No marks yet."); return; }
        for (Mark m : marks) System.out.println("  " + m);
        System.out.printf("  GPA: %.2f%n", gpa);
    }

    public void viewTranscript() {
        System.out.println("| TRANSCRIPT |");
        System.out.println("Student: " + getFirstName() + " " + getLastName());
        System.out.println("Major: " + major + " | Year: " + yearOfStudy + " | Degree: " + degree);
        System.out.printf("GPA: %.2f | Fails: %d | Credits left: %d%n", gpa, failCount, totalCredits);
        System.out.println("Courses:");
        for (Mark m : marks) System.out.println("  " + m);
        System.out.println("========================");
    }

    public void rateTeacher(Teacher teacher, double rating) {
        teacher.addRating(rating);
        System.out.println("Rated " + teacher.getFirstName() + ": " + rating);
    }

    public void generateReport() {
        System.out.println("=== Academic Report: " + getFirstName() + " " + getLastName() + " ===");
        long passed = marks.stream().filter(Mark::isPassed).count();
        long failed = marks.stream().filter(m -> !m.isPassed()).count();
        System.out.println("Passed: " + passed + " | Failed: " + failed);
        System.out.printf("GPA: %.2f%n", gpa);
    }

    public void becomeResearcher(ResearcherDecorator decorator) {
        this.researcherRole = decorator;
        System.out.println(getFirstName() + " is now a Researcher.");
    }

    public boolean isResearcher() { return researcherRole != null; }

    // Calculates credit-weighted average GPA based on 4.0 system scale
    public void updateGpa() {
        if (marks.isEmpty()) {
            gpa = 0.0;
            return;
        }

        double totalGradePoints = 0.0;
        int attemptedCredits = 0;

        for (Mark m : marks) {
            Course course = m.getCourse();
            if (course != null) {
                double totalScore = m.computeTotal();
                if (totalScore > 100) totalScore = 100; // Cap off inputs at max ceiling percentage

                double points = getGradePoints(totalScore);
                totalGradePoints += (points * course.getCredits());
                attemptedCredits += course.getCredits();
            }
        }

        if (attemptedCredits > 0) {
            gpa = totalGradePoints / attemptedCredits;
        } else {
            gpa = 0.0;
        }
    }

    public void update(Mark mark) {
        if (!mark.isPassed()) {
            failCount++;
            if (failCount >= MAX_FAILS) {
                System.out.println("WARNING: " + getFirstName() + " has failed " + failCount + " times!");
            }
        }
        updateGpa();
    }

    @Override
    public UserType getRole() { return UserType.STUDENT; }

    @Override
    public void displayDashboard() {
        System.out.println("=== Student Dashboard: " + getFirstName() + " ===");
        System.out.println("Major: " + major + " | Year: " + yearOfStudy);
        System.out.printf("GPA: %.2f | Credits left: %d | Fails: %d%n", gpa, totalCredits, failCount);
        System.out.println("Registered courses: " + registeredCourses.size());
    }

    // Getters and setters
    public String getStudentId() { return studentId; }
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
    public int getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(int y) { this.yearOfStudy = y; }
    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }
    public int getTotalCredits() { return totalCredits; }
    public void setTotalCredits(int c) { this.totalCredits = c; }
    public int getFailCount() { return failCount; }
    public List<Course> getRegisteredCourses() { return registeredCourses; }
    public List<Mark> getMarks() { return marks; }
    public List<String> getOrganizations() { return organizations; }
    public boolean isOrganizationHead() { return isOrganizationHead; }
    public void setOrganizationHead(boolean h) { this.isOrganizationHead = h; }
    public Degree getDegree() { return degree; }
    public Researcher getResearcherRole() { return researcherRole; }
}