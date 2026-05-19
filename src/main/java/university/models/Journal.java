package university.models;

import university.interfaces.Observable;
import university.interfaces.Subscriber;
import java.io.Serializable;
import java.util.*;

public class Journal implements Observable, Serializable {

    private String journalName;
    private List<ResearchPaper> papers;
    private List<Subscriber> subscribers;
    private String faculty;

    public Journal(String journalName, String faculty) {
        this.journalName = journalName;
        this.faculty = faculty;
        this.papers = new ArrayList<>();
        this.subscribers = new ArrayList<>();
    }

    public void publishPaper(ResearchPaper paper) {
        papers.add(paper);
        notifySubscribers(paper);
        System.out.println("Paper published in " + journalName + ": " + paper.getTitle());
    }

    @Override
    public void subscribe(Subscriber s) {
        if (!subscribers.contains(s)) {
            subscribers.add(s);
            System.out.println("Subscribed to journal: " + journalName);
        }
    }

    @Override
    public void unsubscribe(Subscriber s) {
        subscribers.remove(s);
        System.out.println("Unsubscribed from journal: " + journalName);
    }

    @Override
    public void notifySubscribers(ResearchPaper paper) {
        for (Subscriber s : subscribers) {
            s.update(journalName, paper);
        }
    }

    public String getJournalName() { return journalName; }
    public void setJournalName(String n) { this.journalName = n; }
    public List<ResearchPaper> getPapers() { return papers; }
    public List<Subscriber> getSubscribers() { return subscribers; }
    public String getFaculty() { return faculty; }
    public void setFaculty(String f) { this.faculty = f; }

    @Override
    public String toString() {
        return journalName + " [" + faculty + "] | Papers: " + papers.size() + " | Subscribers: " + subscribers.size();
    }
}
