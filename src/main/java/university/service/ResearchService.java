package university.service;

import university.interfaces.Researcher;
import university.models.*;
import university.storage.DataStorage;
import java.util.*;
import java.util.stream.Collectors;

public class ResearchService {

    public void printAllPapers(Comparator<ResearchPaper> comparator) {
        List<ResearchPaper> all = new ArrayList<>(DataStorage.getInstance().getAllPapers());
        all.sort(comparator);
        System.out.println("| All Research Papers |");
        for (ResearchPaper p : all) System.out.println("  " + p);
    }

    public Researcher getTopCitedResearcher() {
        List<User> users = new ArrayList<>(DataStorage.getInstance().getAllUsers());
        Researcher top = null;
        int maxCitations = -1;
        for (User u : users) {
            Researcher r = getResearcherRole(u);
            if (r != null) {
                int totalCitations = r.getResearchPapers().stream().mapToInt(ResearchPaper::getCitations).sum();
                if (totalCitations > maxCitations) {
                    maxCitations = totalCitations;
                    top = r;
                }
            }
        }
        if (top != null) System.out.println("Top cited researcher: " + top);
        return top;
    }

    public Researcher getTopCitedBySchool(String school) {
        List<User> users = new ArrayList<>(DataStorage.getInstance().getAllUsers());
        Researcher top = null;
        int maxCitations = -1;
        for (User u : users) {
            if (u instanceof Student && !((Student) u).getMajor().equalsIgnoreCase(school))
                continue;
            Researcher r = getResearcherRole(u);
            if (r != null) {
                int total = r.getResearchPapers().stream()
                    .mapToInt(ResearchPaper::getCitations).sum();
                if (total > maxCitations) {
                    maxCitations = total;
                    top = r;
                }
            }
        }
        return top;
    }

    private Researcher getResearcherRole(User u) {
        if (u instanceof GraduateStudent) return (GraduateStudent) u;
        if (u instanceof Student && ((Student) u).isResearcher())
            return (Researcher) ((Student) u).getResearcherRole();
        if (u instanceof Teacher && ((Teacher) u).isResearcher())
            return (Researcher) ((Teacher) u).getResearcherRole();
        return null;
    }

    public void generateTopCitedNews() {
        Researcher top = getTopCitedResearcher();
        if (top == null) return;
        String name = top.toString();
        News news = new News("Top Cited Researcher: " + name, "The most cited researcher this period is: " + name + " with h-index: " + top.calculateHIndex(), "Research");
        DataStorage.getInstance().addNews(news);
        System.out.println("Top-cited researcher news generated and pinned.");
    }
}
