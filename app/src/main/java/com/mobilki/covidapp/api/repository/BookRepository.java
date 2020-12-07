package com.mobilki.covidapp.api.repository;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.mobilki.covidapp.api.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookRepository implements Repository<Book> {
    private List<Book> books = new ArrayList<>();

    @Override
    public void add(Book element) {
        books.add(element);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Book get(String isbn) {
        return books.stream()
                .filter(b -> b.getId().equals(isbn))
                .findAny()
                .orElse(null);
    }

    @Override
    public List<Book> getAll() {
        return books;
    }
}
