package university.models;

import java.io.Serializable;

public abstract class Employee extends User implements Serializable {

    private String employeeId;
    private double salary;

    public Employee(String userId, String firstName, String lastName, String email, String passwordHash, String employeeId, double salary) {
        super(userId, firstName, lastName, email, passwordHash);
        this.employeeId = employeeId;
        this.salary = salary;
    }

    public Employee() { super(); }

    public void sendMessage(User recipient, String content) {
        super.sendMessage(recipient, content);
    }

    public void receiveMessage(Message msg) {
        this.inbox.add(msg);
        System.out.println("New message received from " + msg.getSender().getFirstName());
    }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    @Override
    public String toString() {
        return super.toString() + " | Salary: " + salary;
    }
}
