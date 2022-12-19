package com.home.notes.recycler;

import com.home.notes.data.Note;

public interface PopupMenuClickListener {
    void click(int command, Note note, int position);
}
