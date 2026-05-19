package university.models;

import university.enums.Format;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ResearchPaper implements Serializable, Comparable<ResearchPaper> {

    private String doi;
    private String title;
    private List<String> authors;
    private String journal;
    private int pages;
    private LocalDate datePublished;
    private int citations;

    public ResearchPaper(String doi, String title, List<String> authors, String journal, int pages, LocalDate datePublished) {
        this.doi = doi;
        this.title = title;
        this.authors = new ArrayList<>(authors);
        this.journal = journal;
        this.pages = pages;
        this.datePublished = datePublished;
        this.citations = 0;
    }

    public String getCitation(Format format) {
        if (format == Format.PLAIN_TEXT) {
            return String.join(", ", authors) + ". (" + datePublished.getYear() + "). " + title + ". " + journal + ". " + pages + " pages. DOI: " + doi;
        } else {
            return "@article{" + doi + ",\n" + "  author = {" + String.join(" and ", authors) + "},\n" + "  title = {" + title + "},\n" + "  journal = {" + journal + "},\n" + "  year = {" + datePublished.getYear() + "},\n" + "  pages = {" + pages + "},\n" + "  doi = {" + doi + "}\n" + "}";
        }
    }

    public void addCitation() {
        this.citations++;
    }

    // Static comparators
    public static Comparator<ResearchPaper> byDate() {
        return Comparator.comparing(ResearchPaper::getDatePublished);
    }

    public static Comparator<ResearchPaper> byCitations() {
        return Comparator.comparingInt(ResearchPaper::getCitations).reversed();
    }

    public static Comparator<ResearchPaper> byLength() {
        return Comparator.comparingInt(ResearchPaper::getPages).reversed();
    }

    @Override
    public int compareTo(ResearchPaper other) {
        return Integer.compare(other.citations, this.citations);
    }

    // Getters
    public String getDoi() { return doi; }
    public void setDoi(String doi) { this.doi = doi; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public List<String> getAuthors() { return authors; }
    public String getJournal() { return journal; }
    public void setJournal(String journal) { this.journal = journal; }
    public int getPages() { return pages; }
    public void setPages(int pages) { this.pages = pages; }
    public LocalDate getDatePublished() { return datePublished; }
    public void setDatePublished(LocalDate datePublished) { this.datePublished = datePublished; }
    public int getCitations() { return citations; }
    public void setCitations(int citations) { this.citations = citations; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResearchPaper)) return false;
        return Objects.equals(doi, ((ResearchPaper) o).doi);
    }

    @Override
    public int hashCode() { return Objects.hash(doi); }

    @Override
    public String toString() {
        return "\"" + title + "\" by " + String.join(", ", authors) + " | " + journal + " | " + datePublished.getYear() + " | Citations: " + citations;
    }
}
