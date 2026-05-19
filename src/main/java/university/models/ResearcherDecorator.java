package university.models;

import university.exceptions.NonResearcherException;
import university.interfaces.Researcher;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ResearcherDecorator implements Researcher, Serializable {

    private Employee wrappedEmployee;
    private List<ResearchPaper> researchPapers;
    private List<ResearchProject> researchProjects;

    public ResearcherDecorator(Employee employee) {
        this.wrappedEmployee = employee;
        this.researchPapers = new ArrayList<>();
        this.researchProjects = new ArrayList<>();
    }

    @Override
    public int calculateHIndex() {
        int h = 0;
        List<ResearchPaper> sorted = new ArrayList<>(researchPapers);
        sorted.sort(ResearchPaper.byCitations());
        for (int i = 0; i < sorted.size(); i++) {
            if (sorted.get(i).getCitations() >= i + 1) {
                h = i + 1;
            } else {
                break;
            }
        }
        return h;
    }

    @Override
    public void printPapers(Comparator<ResearchPaper> c) {
        List<ResearchPaper> sorted = new ArrayList<>(researchPapers);
        sorted.sort(c);
        System.out.println("| Research Papers of " + wrappedEmployee.getFirstName() + " " + wrappedEmployee.getLastName() + " |");
        for (ResearchPaper p : sorted) {
            System.out.println("  " + p);
        }
    }

    @Override
    public List<ResearchPaper> getResearchPapers() { return researchPapers; }

    @Override
    public void addResearchPaper(ResearchPaper paper) {
        researchPapers.add(paper);
    }

    @Override
    public List<ResearchProject> getResearchProjects() { return researchProjects; }

    @Override
    public void joinResearchProject(ResearchProject project) throws NonResearcherException {
        project.addParticipant(this);
        researchProjects.add(project);
    }

    public Employee getWrappedEmployee() { return wrappedEmployee; }

    @Override
    public String toString() {
        return "Researcher[" + wrappedEmployee.getFirstName() + " "
            + wrappedEmployee.getLastName() + "] h-index=" + calculateHIndex();
    }
}
