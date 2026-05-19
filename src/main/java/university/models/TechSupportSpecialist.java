package university.models;

import university.enums.RequestStatus;
import university.enums.UserType;
import java.util.*;

public class TechSupportSpecialist extends User {

    private List<TechRequest> requests;

    public TechSupportSpecialist(String userId, String firstName, String lastName, String email, String passwordHash) {
        super(userId, firstName, lastName, email, passwordHash);
        this.requests = new ArrayList<>();
    }

    public void receiveRequest(TechRequest request) {
        request.setStatus(RequestStatus.VIEWED);
        requests.add(request);
        System.out.println("Request received and set to VIEWED: " + request.getDescription());
    }

    public void acceptRequest(TechRequest request) {
        request.setStatus(RequestStatus.ACCEPTED);
        System.out.println("Request ACCEPTED: " + request.getDescription());
    }

    public void rejectRequest(TechRequest request) {
        request.setStatus(RequestStatus.REJECTED);
        System.out.println("Request REJECTED: " + request.getDescription());
    }

    public void markDone(TechRequest request) {
        request.setStatus(RequestStatus.DONE);
        System.out.println("Request DONE: " + request.getDescription());
    }

    public void viewNewRequests() {
        System.out.println("| New Requests |");
        for (TechRequest r : requests) {
            if (r.getStatus() == RequestStatus.NEW || r.getStatus() == RequestStatus.VIEWED) {
                System.out.println("  " + r);
            }
        }
    }

    @Override
    public UserType getRole() { return UserType.TECH_SUPPORT; }

    @Override
    public void displayDashboard() {
        System.out.println("=== Tech Support Dashboard: " + getFirstName() + " ===");
        System.out.println("Total requests: " + requests.size());

        for (RequestStatus s : RequestStatus.values()) {
            int count = 0;
            for (TechRequest r : requests) {
                if (r.getStatus() == s) {
                    count++;
                }
            }
            System.out.println("  " + s + ": " + count);
        }
    }

    public List<TechRequest> getRequests() { return requests; }
}