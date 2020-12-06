package com.mobilki.covidapp.api.repository;

import java.util.List;

public interface Repository<T> {
    void add(T element);
    T get(String id);
    List<T> getAll();
}
