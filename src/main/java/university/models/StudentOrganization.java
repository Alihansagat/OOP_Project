package university.models;

import java.io.Serializable;
import java.util.*;

public class StudentOrganization implements Serializable {
    private static int nextId = 1;

    private String organizationId;
    private String name;
    private String headStudentId;
    private List<Student> members;

    public StudentOrganization(String name) {
        this.organizationId = "O" + nextId++;
        this.name = name;
        this.members = new ArrayList<>();
    }

    public void addMember(Student student) {
        if (!members.contains(student)) {
            members.add(student);
            student.getOrganizations().add(name);
            System.out.println(student.getFirstName() + " joined organization: " + name);
        }
    }

    public void setHead(Student student) {
        if (!members.contains(student)) addMember(student);
        this.headStudentId = student.getStudentId();
        student.setOrganizationHead(true);
        System.out.println(student.getFirstName() + " is now head of: " + name);
    }

    public List<Student> getMembers() { return members; }
    public String getOrganizationId() { return organizationId; }
    public String getName() { return name; }
    public String getHeadStudentId() { return headStudentId; }

    @Override
    public String toString() {
        return name + " | Members: " + members.size() + " | Head ID: " + headStudentId;
    }
}
