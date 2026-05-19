package university.interfaces;

import university.models.ResearchPaper;
import university.models.ResearchProject;
import java.util.Comparator;
import java.util.List;

public interface Researcher {
    int calculateHIndex();
    void printPapers(Comparator<ResearchPaper> c);
    List<ResearchPaper> getResearchPapers();
    void addResearchPaper(ResearchPaper paper);
    List<ResearchProject> getResearchProjects();
    void joinResearchProject(ResearchProject project) throws university.exceptions.NonResearcherException;
}
