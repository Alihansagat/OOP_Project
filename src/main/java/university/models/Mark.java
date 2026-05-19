package university.models;

import java.io.Serializable;

public class Mark implements Serializable, Comparable<Mark> {

    private Student student;
    private Course course;
    private double firstAttestation;
    private double secondAttestation;
    private double finalExam;

    public Mark(Student student, Course course) {
        this.student = student;
        this.course = course;
    }

    public double computeTotal() {
        return firstAttestation + secondAttestation + finalExam;
    }

    public boolean isPassed() {
        return computeTotal() >= 50.0;
    }

    public String getLetterGrade() {
        double total = computeTotal();
        if (total >= 90) return "A";
        if (total >= 80) return "B";
        if (total >= 70) return "C";
        if (total >= 60) return "D";
        return "F";
    }

    @Override
    public int compareTo(Mark other) {
        return Double.compare(other.computeTotal(), this.computeTotal());
    }

    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public double getFirstAttestation() { return firstAttestation; }
    public void setFirstAttestation(double v) { this.firstAttestation = v; }
    public double getSecondAttestation() { return secondAttestation; }
    public void setSecondAttestation(double v) { this.secondAttestation = v; }
    public double getFinalExam() { return finalExam; }
    public void setFinalExam(double v) { this.finalExam = v; }

    @Override
    public String toString() {
        return course.getName() + " | Att1: " + firstAttestation + " | Att2: " + secondAttestation + " | Final: " + finalExam + " | Total: " + computeTotal() + " (" + getLetterGrade() + ")";
    }
}
