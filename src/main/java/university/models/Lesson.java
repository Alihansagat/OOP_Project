package university.models;

import university.enums.LessonType;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Lesson implements Serializable {
    private static int nextId = 1;

    private String lessonId;
    private Course course;
    private Teacher teacher;
    private LessonType lessonType;
    private LocalDateTime dateTime;
    private String room;

    public Lesson(Course course, Teacher teacher, LessonType lessonType, LocalDateTime dateTime, String room) {
        this.lessonId = "L" + nextId++;
        this.course = course;
        this.teacher = teacher;
        this.lessonType = lessonType;
        this.dateTime = dateTime;
        this.room = room;
    }

    public void setRoom(String room) { this.room = room; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public String getLessonId() { return lessonId; }
    public Course getCourse() { return course; }
    public Teacher getTeacher() { return teacher; }
    public LessonType getLessonType() { return lessonType; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getRoom() { return room; }

    @Override
    public String toString() {
        return lessonType + " | " + course.getName() + " | Room: " + room + " | " + dateTime;
    }
}