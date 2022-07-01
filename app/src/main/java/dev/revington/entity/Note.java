package dev.revington.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;

@Document(collection = "notes")
public class Note {

    private int Id;
    private String title;
    private String description;
    private Timestamp time;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
