package com.mobilki.covidapp.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Book implements Serializable {
    private String id;
    private String title;
    private String publicationDate;
    private String publisher;
    private String pages;
    private String description;
    private String ratings;
    private String ratingsCount;
    private List<String> genres;
    private String imageUrl;
    private boolean pdfAvailable;
    private List<String> authors;

    public Book(String id) {
        this.id = id;
        authors = new ArrayList<>();
        genres = new ArrayList<>();
    }

    public void addAuthor(String name) { authors.add(name); }
    public void addGenre(String name) { genres.add(name); }
}
