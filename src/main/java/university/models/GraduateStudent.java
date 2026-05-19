package university.models;

import university.enums.Degree;
import university.exceptions.InvalidSupervisorException;
import university.exceptions.NonResearcherException;
import university.interfaces.Researcher;
import java.util.*;

public class GraduateStudent extends Student implements Researcher {
    private static final int MIN_SUPERVISOR_HINDEX = 3;

    private Researcher supervisor;
    private List<ResearchPaper> researchPapers;
    private List<ResearchProject> researchProjects;
    private List<ResearchPaper> diplomaPapers;

    public GraduateStudent(String userId, String firstName, String lastName, String email, String passwordHash, String studentId, String major, int yearOfStudy, Degree degree) {
        super(userId, firstName, lastName, email, passwordHash, studentId, major, yearOfStudy, degree);
        this.researchPapers = new ArrayList<>();
        this.researchProjects = new ArrayList<>();
        this.diplomaPapers = new ArrayList<>();
    }

    public void assignSupervisor(Researcher supervisor) throws InvalidSupervisorException {
        if (supervisor.calculateHIndex() < MIN_SUPERVISOR_HINDEX) {
            throw new InvalidSupervisorException(
                "Supervisor's h-index (" + supervisor.calculateHIndex() + ") is less than required minimum of " + MIN_SUPERVISOR_HINDEX);
        }
        this.supervisor = supervisor;
        System.out.println("Supervisor assigned successfully. H-index: " + supervisor.calculateHIndex());
    }

    public void addDiplomaPaper(ResearchPaper paper) {
        diplomaPapers.add(paper);
        researchPapers.add(paper);
        System.out.println("Diploma paper added: " + paper.getTitle());
    }

    @Override
    public int calculateHIndex() {
        int h = 0;
        List<ResearchPaper> sorted = new ArrayList<>(researchPapers);
        sorted.sort(ResearchPaper.byCitations());
        for (int i = 0; i < sorted.size(); i++) {
            if (sorted.get(i).getCitations() >= i + 1) h = i + 1;
            else break;
        }
        return h;
    }

    @Override
    public void printPapers(Comparator<ResearchPaper> c) {
        List<ResearchPaper> sorted = new ArrayList<>(researchPapers);
        sorted.sort(c);
        System.out.println("Papers of " + getFirstName() + " " + getLastName());
        for (ResearchPaper p : sorted) System.out.println("  " + p);
    }

    @Override
    public List<ResearchPaper> getResearchPapers() { return researchPapers; }

    @Override
    public void addResearchPaper(ResearchPaper paper) { researchPapers.add(paper); }

    @Override
    public List<ResearchProject> getResearchProjects() { return researchProjects; }

    @Override
    public void joinResearchProject(ResearchProject project) throws NonResearcherException {
        project.addParticipant(this);
        researchProjects.add(project);
        System.out.println(getFirstName() + " joined project: " + project.getTopic());
    }

    public Researcher getSupervisor() { return supervisor; }
    public List<ResearchPaper> getDiplomaPapers() { return diplomaPapers; }
}
