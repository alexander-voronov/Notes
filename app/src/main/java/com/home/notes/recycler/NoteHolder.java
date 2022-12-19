package com.home.notes.recycler;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.home.notes.R;
import com.home.notes.data.Note;

public class NoteHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {

    private TextView title;
    private TextView description;
    private TextView importance;
    private TextView date;
    private ImageView noteMenu;

    private PopupMenu popupMenu;

    private Note note;

    private PopupMenuClickListener listener;


    public NoteHolder(@NonNull View itemView, PopupMenuClickListener listener) {
        super(itemView);
        this.listener = listener;
        title = itemView.findViewById(R.id.note_title);
        description = itemView.findViewById(R.id.note_description);
        importance = itemView.findViewById(R.id.note_importance_level);
        date = itemView.findViewById(R.id.note_date);
        noteMenu = itemView.findViewById(R.id.note_menu);

        popupMenu = new PopupMenu(itemView.getContext(), noteMenu);
        popupMenu.inflate(R.menu.note_context);
        noteMenu.setOnClickListener(v -> popupMenu.show());
        popupMenu.setOnMenuItemClickListener(this);


/*
                itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNoteClick(note);
            }
        });*/

    }

    void bind(Note note) {
        this.note = note;
        title.setText(note.getTitle());
        description.setText(note.getDescription());
        importance.setText(note.getImportance());
        date.setText(note.getDate());

    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_modify:
              listener.click(R.id.context_modify,note,getAdapterPosition());
                return true;
                //  Toast.makeText(itemView.getContext(), "Modify", Toast.LENGTH_SHORT).show();
            case R.id.context_delete:
                listener.click(R.id.context_delete,note,getAdapterPosition());
                return true;
                //Toast.makeText(itemView.getContext(), "Delete", Toast.LENGTH_SHORT).show();
            default:
                return false;
        }

    }
}
