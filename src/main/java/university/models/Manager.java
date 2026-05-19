package university.models;

import university.enums.ManagerType;
import university.enums.UserType;
import java.util.*;

public class Manager extends Employee {

    private ManagerType managerType;
    private List<Complaint> complaints;

    public Manager(String userId, String firstName, String lastName,
                   String email, String passwordHash,
                   String employeeId, double salary, ManagerType managerType) {
        super(userId, firstName, lastName, email, passwordHash, employeeId, salary);
        this.managerType = managerType;
        this.complaints = new ArrayList<>();
    }

    public void assignCourseToTeacher(Course course, Teacher teacher) {
        teacher.addCourse(course);
        course.addTeacher(teacher);
        System.out.println("Assigned " + teacher.getFirstName() + " to " + course.getName());
    }

    public void approveStudentRegistration(Student student, Course course) {
        course.setOpen(true);
        System.out.println("Registration approved for: " + course.getName());
    }

    public void addCourseForRegistration(Course course, String targetMajor, int targetYear) {
        course.setTargetMajor(targetMajor);
        course.setTargetYear(targetYear);
        course.setOpen(true);
        System.out.println("Course opened for registration: " + course.getName());
    }

    public void generateAcademicReport(List<Student> students) {
        System.out.println("| ACADEMIC REPORT |");
        System.out.printf("%-25s %-10s %-10s %-8s%n", "Name", "Major", "GPA", "Fails");
        System.out.println("-".repeat(55));

        for (Student s : students) {
            System.out.printf("%-25s %-10s %-10.2f %-8d%n",
                    s.getFirstName() + " " + s.getLastName(),
                    s.getMajor(),
                    s.getGpa(),
                    s.getFailCount());
        }

        // calculate average GPA manually
        double total = 0;
        for (Student s : students) {
            total += s.getGpa();
        }
        double avgGpa = 0;
        if (!students.isEmpty()) {
            avgGpa = total / students.size();
        }

        System.out.printf("Average GPA: %.2f%n", avgGpa);
        System.out.println("=============================");
    }

    public void manageNews(List<News> newsList, String action, News news) {
        if (action.equals("add")) {
            newsList.add(news);
            Collections.sort(newsList);
            System.out.println("News added: " + news.getTitle());
        } else if (action.equals("remove")) {
            newsList.remove(news);
            System.out.println("News removed.");
        }
    }

    public void viewStudentsByGpa(List<Student> students) {
        System.out.println("| Students by GPA |");

        // sort by GPA highest first - bubble sort
        for (int i = 0; i < students.size() - 1; i++) {
            for (int j = i + 1; j < students.size(); j++) {
                if (students.get(i).getGpa() < students.get(j).getGpa()) {
                    Student temp = students.get(i);
                    students.set(i, students.get(j));
                    students.set(j, temp);
                }
            }
        }

        for (Student s : students) {
            System.out.printf("  %s %s - GPA: %.2f%n",
                    s.getFirstName(), s.getLastName(), s.getGpa());
        }
    }

    public void viewStudentsAlphabetically(List<Student> students) {
        System.out.println("| Students Alphabetically |");

        // sort by last name, then first name - bubble sort
        for (int i = 0; i < students.size() - 1; i++) {
            for (int j = i + 1; j < students.size(); j++) {
                String name1 = students.get(i).getLastName() + students.get(i).getFirstName();
                String name2 = students.get(j).getLastName() + students.get(j).getFirstName();
                if (name1.compareTo(name2) > 0) {
                    Student temp = students.get(i);
                    students.set(i, students.get(j));
                    students.set(j, temp);
                }
            }
        }

        for (Student s : students) {
            System.out.println("  " + s.getLastName() + ", " + s.getFirstName());
        }
    }

    public void viewTechRequests(List<TechRequest> requests) {
        System.out.println("| Tech Requests |");
        for (TechRequest r : requests) {
            System.out.println("  " + r);
        }
    }

    public void receiveComplaint(Complaint complaint) {
        complaints.add(complaint);
        Collections.sort(complaints);
        System.out.println("Complaint received: " + complaint.getUrgency());
    }

    public void viewComplaints() {
        System.out.println("| Complaints (sorted by urgency) |");
        for (Complaint c : complaints) {
            System.out.println("  " + c);
        }
    }

    @Override
    public UserType getRole() { return UserType.MANAGER; }

    @Override
    public void displayDashboard() {
        System.out.println("| Manager Dashboard: " + getFirstName() + " |");
        System.out.println("Type: " + managerType);
        System.out.println("Pending complaints: " + complaints.size());
    }

    public ManagerType getManagerType() { return managerType; }
    public List<Complaint> getComplaints() { return complaints; }
}