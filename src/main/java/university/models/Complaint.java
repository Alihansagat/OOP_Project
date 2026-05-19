package university.models;

import university.enums.UrgencyLevel;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Complaint implements Serializable, Comparable<Complaint> {

    private String complaintId;
    private Teacher sender;
    private Student student;
    private String reason;
    private UrgencyLevel urgency;
    private LocalDateTime createdAt;

    public Complaint(String complaintId, Teacher sender, Student student, String reason, UrgencyLevel urgency, LocalDateTime createdAt) {
        this.complaintId = complaintId;
        this.sender = sender;
        this.student = student;
        this.reason = reason;
        this.urgency = urgency;
        this.createdAt = createdAt;
    }

    public UrgencyLevel getUrgency() { return urgency; }
    public String getComplaintId() { return complaintId; }
    public Teacher getSender() { return sender; }
    public Student getStudent() { return student; }
    public String getReason() { return reason; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public int compareTo(Complaint other) {
        return other.urgency.ordinal() - this.urgency.ordinal();
    }

    @Override
    public String toString() {
        return urgency + " From: " + sender.getFirstName() + " | Student: " + student.getFirstName() + " " + student.getLastName() + "\n  Reason: " + reason + " | " + createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Complaint)) return false;
        return Objects.equals(complaintId, ((Complaint) o).complaintId);
    }

    @Override
    public int hashCode() { return Objects.hash(complaintId); }
}
