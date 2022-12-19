package com.home.notes.data;

import java.util.List;

public interface Repo {

    void create(Note note);

    Note read(int id);

    void update(Note note);

    void delete(int id);

    List<Note> getAll();

}