package com.mobilki.covidapp.api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class Book {
    private String id;
    private String title;
    private Date publicationDate;
    private String publisher;
    private int pages;
    private String description;
    private double ratings;
    private int ratingsCount;
    private List<String> genres;
    private String imageUrl;
    private boolean pdfAvailable;
    private List<String> authors;

    public Book(String id) {
        this.id = id;
        authors = new ArrayList<>();
    }

    public void addAuthor(String name) { authors.add(name); }
}
