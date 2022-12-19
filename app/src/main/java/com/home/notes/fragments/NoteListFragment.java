package com.home.notes.fragments;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.service.controls.templates.ControlButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.home.notes.R;
import com.home.notes.data.Constans;
import com.home.notes.data.InMemoryRepoImp;
import com.home.notes.data.Note;
import com.home.notes.data.Repo;
import com.home.notes.dialogs.NoteDialog;
import com.home.notes.recycler.NoteAdapter;
import com.home.notes.recycler.PopupMenuClickListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NoteListFragment extends Fragment implements PopupMenuClickListener {

    private Repo repository = InMemoryRepoImp.getInstance();
    private RecyclerView list;
    private NoteAdapter adapter;
    private SharedPreferences pref;
    private Gson gson =  new Gson();


    public NoteListFragment() {
        // Required empty public constructor
    }


    public static NoteListFragment newInstance(Note note) {
        NoteListFragment fragment = new NoteListFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constans.NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Constans.TAG, "onCreate()NOTE LIST");
        pref = PreferenceManager.getDefaultSharedPreferences(requireContext());

        requireActivity().getSupportFragmentManager().setFragmentResultListener(Constans.REQUEST_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Note resultNote = (Note) result.getSerializable(Constans.NOTE);
                if (resultNote.getId() != -1) {
                    Log.d(Constans.TAG, "onFragmentResult() UPDATE ITEM!");
                    repository.update(resultNote);
                    adapter.notifyItemChanged(resultNote.getId());
                    saveRepo(repository);

                } else {
                    Log.d(Constans.TAG, "onFragmentResult() NEW ITEM!");
                    repository.create(resultNote);
                    adapter.notifyItemInserted(resultNote.getId());
                    saveRepo(repository);

                }

            }
        });

    }

    private void saveRepo(Repo repository) {
        String notesString = gson.toJson(repository);
        pref
                .edit()
                .putString(Constans.PREF_KEY,notesString)
                .apply();
    }


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_list, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String  notesString = pref.getString(Constans.PREF_KEY,"");
        //
        if (notesString.isEmpty()) {
            Toast.makeText(requireActivity(), "No saved data", Toast.LENGTH_SHORT).show();
        } else {
            try {
                repository = gson.fromJson(notesString, new TypeToken<InMemoryRepoImp>(){}.getType());

            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }




        }


        if (((InMemoryRepoImp) repository).getCounter() == 0) {
          //  fillRepo();
        }


        adapter = new NoteAdapter();
        adapter.setNotes(repository.getAll());


        //  adapter.setOnNoteClickListener(this::onNoteClick);
        adapter.setOnPopupMenuItemClickListener(this);
        list = view.findViewById(R.id.recycler_note_list);
        list.setLayoutManager(new LinearLayoutManager(this.getContext()));
        list.setAdapter(adapter);
    }

    private void fillRepo() {
        repository.create(new Note("Title1", "Description 1", "high", "11/11/22"));
        repository.create(new Note("Title2", "Description 2", "high", "11/11/22"));
        repository.create(new Note("Title3", "Description 3", "high", "11/11/22"));
        repository.create(new Note("Title4", "Description 4", "high", "11/11/22"));
        repository.create(new Note("Title5", "Description 5", "low", "11/11/22"));
        repository.create(new Note("Title6", "Description 6", "low", "11/11/22"));
        repository.create(new Note("Title7", "Description 7", "low", "11/11/22"));
        repository.create(new Note("Title8", "Description 8", "low", "11/11/22"));
        repository.create(new Note("Title9", "Description 9", "low", "11/11/22"));
        repository.create(new Note("Title10", "Description 10", "normal", "11/11/22"));
        repository.create(new Note("Title11", "Description 11", "normal", "11/11/22"));
        repository.create(new Note("Title12", "Description 12", "normal", "11/11/22"));
        repository.create(new Note("Title13", "Description 13", "normal", "11/11/22"));
        repository.create(new Note("Title14", "Description 14", "normal", "11/11/22"));
        repository.create(new Note("Title15", "Description 15", "normal", "11/11/22"));

    }




    @Override
    public void click(int command, Note note, int position) {
        switch (command) {

            case R.id.context_modify:
                Log.d(Constans.TAG, "modify option");
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                {
                    NoteDialog.getInstance(note).show(requireActivity().getSupportFragmentManager(), Constans.DIALOG_NOTE);
                } else
                {
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.detail_fragment_holder, EditNoteFragment.newInstance(note))
                            .addToBackStack(null)
                            .commit();
                }

                return;

            case R.id.context_delete:
                repository.delete(note.getId());
                adapter.delete(repository.getAll(), position);
                saveRepo(repository);
                Log.d(Constans.TAG, "delete option");
                return;
        }


    }


}

