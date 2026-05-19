package university.models;

import university.exceptions.NonResearcherException;
import university.interfaces.Researcher;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ResearchProject implements Serializable {
    private static int nextId = 1;

    private String projectId;
    private String topic;
    private List<ResearchPaper> publishedPapers;
    private List<Researcher> participants;

    public ResearchProject(String topic) {
        this.projectId = "P" + nextId++;
        this.topic = topic;
        this.publishedPapers = new ArrayList<>();
        this.participants = new ArrayList<>();
    }

    public void addParticipant(Researcher researcher) {
        participants.add(researcher);
        System.out.println("Researcher added to project: " + topic);
    }

    public void tryJoin(Object candidate) throws NonResearcherException {
        if (!(candidate instanceof Researcher)) {
            throw new NonResearcherException(
                candidate.getClass().getSimpleName() + " is not a Researcher and cannot join the project.");
        }
        addParticipant((Researcher) candidate);
    }

    public void addPaper(ResearchPaper paper) {
        publishedPapers.add(paper);
        System.out.println("Paper added to project: " + paper.getTitle());
    }

    public String getProjectId() { return projectId; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public List<ResearchPaper> getPublishedPapers() { return publishedPapers; }
    public List<Researcher> getParticipants() { return participants; }

    @Override
    public String toString() {
        return "Project: " + topic + " | Participants: " + participants.size() + " | Papers: " + publishedPapers.size();
    }
}
