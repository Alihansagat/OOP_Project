package university.models;

import university.enums.RequestStatus;
import java.io.Serializable;
import java.time.LocalDateTime;

public class TechRequest implements Serializable {
    private static int nextId = 1;

    private String requestId;
    private User requester;
    private String description;
    private RequestStatus status;
    private LocalDateTime createdAt;

    public TechRequest(User requester, String description) {
        this.requestId = "R" + nextId++;
        this.requester = requester;
        this.description = description;
        this.status = RequestStatus.NEW;
        this.createdAt = LocalDateTime.now();
    }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }
    public String getRequestId() { return requestId; }
    public User getRequester() { return requester; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return "[" + status + "] " + description + " | From: " + requester.getFirstName() + " | " + createdAt;
    }
}
