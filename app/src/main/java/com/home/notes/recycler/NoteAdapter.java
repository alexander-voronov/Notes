package com.home.notes.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.home.notes.R;
import com.home.notes.data.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {

    private List<Note> notes = new ArrayList<>();

    public void setNotes(List<Note> notes) {
        this.notes = notes;

    }

    private PopupMenuClickListener listener;
    public void setOnPopupMenuItemClickListener (PopupMenuClickListener listener)
    {
        this.listener = listener;
    }



   /* public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }
*/

   /* private OnNoteClickListener listener;

    public void setOnNoteClickListener(OnNoteClickListener listener) {
        this.listener = listener;
    }
*/

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.note_item, parent, false);
        return new NoteHolder(view, listener);

    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        holder.bind(notes.get(position));


    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void delete(List<Note> all, int position) {
        notes = all;
        notifyItemRemoved(position);
    }


}
