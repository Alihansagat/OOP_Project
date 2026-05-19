package university.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class News implements Serializable, Comparable<News> {
    private static int nextId = 1;

    private String newsId;
    private String title;
    private String content;
    private String topic;
    private boolean isPinned;
    private LocalDateTime publishedAt;
    private List<String> comments;

    public News(String title, String content, String topic) {
        this.newsId = "N" + nextId++;
        this.title = title;
        this.content = content;
        this.topic = topic;
        this.isPinned = topic.equalsIgnoreCase("Research");
        this.publishedAt = LocalDateTime.now();
        this.comments = new ArrayList<>();
    }

    public void addComment(String comment) {
        comments.add(comment);
    }

    public void setPinned(boolean pinned) { this.isPinned = pinned; }

    // Pinned news comes first, then by date descending
    @Override
    public int compareTo(News other) {
        if (this.isPinned && !other.isPinned) return -1;
        if (!this.isPinned && other.isPinned) return 1;
        return other.publishedAt.compareTo(this.publishedAt);
    }

    public String getNewsId() { return newsId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public boolean isPinned() { return isPinned; }
    public LocalDateTime getPublishedAt() { return publishedAt; }
    public List<String> getComments() { return comments; }

    @Override
    public String toString() {
        return (isPinned ? "[PINNED] " : "") + "[" + topic + "] " + title + "\n  " + content + "\n  Comments: " + comments.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof News)) return false;
        return Objects.equals(newsId, ((News) o).newsId);
    }

    @Override
    public int hashCode() { return Objects.hash(newsId); }
}
