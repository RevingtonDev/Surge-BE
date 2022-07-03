package dev.revington.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;

@Document(collection = "notes")
public class Note {

    private String id;
    private int studentId;
    private String title;
    private String description;
    private long time;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getStudentID() {
        return studentId;
    }

    public void setStudentID(int studentId) {
        this.studentId = studentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
