package com.home.notes.fragments;


import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.button.MaterialButton;
import com.home.notes.R;
import com.home.notes.data.Constans;
import com.home.notes.data.Note;
import com.home.notes.dialogs.DateDialogFragment;

public class CreateNoteFragment extends Fragment {
///
    private EditText title;
    private EditText description;
    private EditText date;
    private MaterialButton createNote;
    private MaterialButton setDate;
    private Spinner importanceSpinner;
    private String importance;
    private int id = -1;


    public CreateNoteFragment() {
        // Required empty public constructor
    }

    public static CreateNoteFragment newInstance() {
        CreateNoteFragment fragment = new CreateNoteFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Constans.TAG, "onCreate() called with Create farag");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);

        title = view.findViewById(R.id.fragment_create_title);
        description = view.findViewById(R.id.fragment_create_description);
        createNote = view.findViewById(R.id.fragment_create_note_button);
        setDate = view.findViewById(R.id.fragment_create_date_button);
        date = view.findViewById(R.id.fragment_create_date);


        requireActivity().getSupportFragmentManager().setFragmentResultListener(Constans.REQUEST_DATE_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String setDate = result.getString(Constans.DATE);
                date.setText(setDate);
            }
        });



        importanceSpinner = view.findViewById(R.id.fragment_create_importance_spinner);
        importanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                importance = (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                importance = (String) parent.getItemAtPosition(1);
            }
        });


        initSpinner();

        createNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                Note createdNote = new Note(id, title.getText().toString(), description.getText().toString(), importance, date.getText().toString());
                result.putSerializable(Constans.NOTE, createdNote);
                requireActivity().getSupportFragmentManager().setFragmentResult(Constans.REQUEST_KEY, result);

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    requireActivity().getSupportFragmentManager().beginTransaction().remove(CreateNoteFragment.this).commit();
                }
            }
        });

        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new DateDialogFragment();
                newFragment.show(requireActivity().getSupportFragmentManager(), "datePicker");
             /*   requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.list_fragment_holder, DateFragment.newInstance())  //EditNoteFragment.newInstance(note))
                        .addToBackStack(null)
                        .commit();*/
            }

        });


    }


    void initSpinner() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.note_importance_level,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        importanceSpinner.setAdapter(adapter);
    }




}
