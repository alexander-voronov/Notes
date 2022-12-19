package com.home.notes.data;

import java.util.ArrayList;
import java.util.List;

public class InMemoryRepoImp implements Repo {
    private static InMemoryRepoImp repo;

    public static Repo getInstance() {
        if (repo == null) {
            repo = new InMemoryRepoImp();
        }
        return repo;
    }


    private ArrayList<Note> notes = new ArrayList<>();

    private int counter = 0;

    public int getCounter() {
        return counter;
    }

    @Override
    public int create(Note note) {
        int id = counter++;
        note.setId(id);
        notes.add(note);
        return id;
    }

    @Override
    public Note read(int id) {
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId() == id)
                return notes.get(i);
        }
        return null;
    }

    @Override
    public void update(Note note) {
        for (int i = 0; i < notes.size(); i++) {

            if (notes.get(i).getId().equals(note.getId()))
            //if (notes.get(i).getId() ==note.getId())
            {
                notes.set(i, note);
                break;
            }

        }

    }

    @Override
    public void delete(int id) {
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId() == id){
                notes.remove(i);
                counter--;
            break;}
        }
    }

    @Override
    public List<Note> getAll() {
        return notes;
    }
}
