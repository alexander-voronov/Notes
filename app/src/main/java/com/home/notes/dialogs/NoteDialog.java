package com.home.notes.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.home.notes.R;
import com.home.notes.data.Constans;
import com.home.notes.data.Note;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NoteDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date.setText(year + "/" + month + "/" + dayOfMonth);
    }

    public interface NoteDialogController {
        void update(Note note);

        void create(String title, String description, String importance, String date);
    }

    private NoteDialogController controller;

    private Note note;
    private String importance;
    TextInputLayout dialogTitle;
    TextInputLayout dialogDescription;
    DatePicker datePicker;
    Spinner importanceSpinner;
    EditText date;



    @Override
    public void onAttach(@NonNull Context context) {

        controller = (NoteDialogController) context;
        super.onAttach(context);
    }

    public static NoteDialog getInstance(Note note) {
        NoteDialog dialog = new NoteDialog();
        Bundle args = new Bundle();
        args.putSerializable(Constans.DIALOG_NOTE_KEY, note);
        dialog.setArguments(args);
        return dialog;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        Bundle args = getArguments();
        note = (Note) args.getSerializable(Constans.DIALOG_NOTE_KEY);
        View dialog = LayoutInflater.from(requireContext()).inflate(R.layout.note_dialog, null);

        dialogTitle = dialog.findViewById(R.id.note_dialog_title);
        dialogDescription = dialog.findViewById(R.id.note_dialog_description);
        datePicker = dialog.findViewById(R.id.dialog_date_picker);
        importanceSpinner = dialog.findViewById(R.id.note_dialog_importance_spinner);
        date = dialog.findViewById(R.id.note_dialog_date);


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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.note_importance_level,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        importanceSpinner.setAdapter(adapter);

        if (note != null) {

            dialogTitle.getEditText().setText(note.getTitle());
            dialogDescription.getEditText().setText(note.getDescription());
            date.setText(note.getDate());

        }
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePicker.init(year, month, day, (
                view,
                yearSelected,
                monthOfYearSelected,
                dayOfMonthSelected) -> date.setText(yearSelected + "/" + (monthOfYearSelected+1) + "/" + dayOfMonthSelected));

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        String buttonText;
        if (note == null) {
            builder.setTitle("Create note");
            buttonText = "Create note";
        } else {
            builder.setTitle("Update note");
            buttonText = "Update note";
        }
        builder.setView(dialog)
                .setCancelable(true)
                .setNegativeButton("Cancel", (dialog1, which) -> dialog1.cancel())
                .setPositiveButton(buttonText, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (note == null) {
                            controller.create(
                                    dialogTitle.getEditText().getText().toString(),
                                    dialogDescription.getEditText().getText().toString(),
                                    importance,
                                    date.getText().toString()
                            );
                        } else {

                            Note updatedNote = new Note(
                                    note.getId(),
                                    dialogTitle.getEditText().getText().toString(),
                                    dialogDescription.getEditText().getText().toString(),
                                    importance,
                                    date.getText().toString()
                            );
                            controller.update(updatedNote);

                        }
                        dialog.dismiss();
                    }

                })
        ;


        return builder.create();
    }
}
