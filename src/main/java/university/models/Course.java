package university.models;

import university.enums.CourseType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;

    private String courseId;
    private String name;
    private int credits;
    private CourseType courseType;
    private String targetMajor;
    private int targetYear;
    private List<Teacher> teachers;
    private List<Lesson> lessons;
    private List<Student> enrolledStudents;
    private boolean isOpen;

    public Course(String name, int credits, CourseType courseType, String targetMajor, int targetYear) {
        this.courseId = "C" + nextId++;
        this.name = name;
        this.credits = credits;
        this.courseType = courseType;
        this.targetMajor = targetMajor;
        this.targetYear = targetYear;
        this.teachers = new ArrayList<>();
        this.lessons = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
        this.isOpen = false;
    }

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }

    public void enrollStudent(Student student) {
        if (!isOpen) {
            System.out.println("Registration is closed for " + name);
            return;
        }
        if (!enrolledStudents.contains(student)) {
            enrolledStudents.add(student);
        }
    }

    public void removeStudent(Student student) {
        enrolledStudents.remove(student);
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    public String getCourseId() { return courseId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    public CourseType getCourseType() { return courseType; }
    public void setCourseType(CourseType courseType) { this.courseType = courseType; }
    public String getTargetMajor() { return targetMajor; }
    public void setTargetMajor(String targetMajor) { this.targetMajor = targetMajor; }
    public int getTargetYear() { return targetYear; }
    public void setTargetYear(int targetYear) { this.targetYear = targetYear; }
    public List<Teacher> getTeachers() { return teachers; }
    public List<Lesson> getLessons() { return lessons; }
    public List<Student> getEnrolledStudents() { return enrolledStudents; }
    public boolean isOpen() { return isOpen; }
    public void setOpen(boolean open) { isOpen = open; }

    @Override
    public String toString() {
        return name + " (" + credits + " credits) [" + courseType + "] for " + targetMajor + " year " + targetYear + " | Open: " + isOpen;
    }
}