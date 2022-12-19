package com.home.notes.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Note implements Serializable {
    private Integer id;
    private String title;
    private String description;
    private String importance;
    private String date;



    public Note(String title, String description, String importance, String date) {
        this.title = title;
        this.description = description;
        this.importance = importance;
        this.date = date;
    }

    public Note(Integer id, String title, String description, String importance,  String date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.importance = importance;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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


    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String  date) {
        this.date = date;
    }
}
