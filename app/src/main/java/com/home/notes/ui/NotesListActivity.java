package com.home.notes.ui;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.navigation.NavigationView;
import com.home.notes.R;
import com.home.notes.data.Constants;
import com.home.notes.data.Note;
import com.home.notes.dialogs.AboutDialog;
import com.home.notes.dialogs.ConfirmExitDialog;
import com.home.notes.dialogs.NoteDialog;
import com.home.notes.fragments.CreateNoteFragment;
import com.home.notes.fragments.NoteListFragment;

public class NotesListActivity extends AppCompatActivity implements NoteDialog.NoteDialogController {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        initToolbarAndDrawer();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.list_fragment_holder, new NoteListFragment())
                .commit();
    }

    private void initToolbarAndDrawer() {
        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        initDrawer(toolbar);
    }

    private void initDrawer(Toolbar toolbar) {
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.navigation_about:
                        new AboutDialog().show(getSupportFragmentManager(), Constants.DIALOG_ABOUT);
                        return true;
                    case R.id.navigation_close:
                        new ConfirmExitDialog().show(getSupportFragmentManager(), Constants.DIALOG_EXIT);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_create) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                NoteDialog.getInstance(null).show(getSupportFragmentManager(), Constants.DIALOG_NOTE);
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.detail_fragment_holder, new CreateNoteFragment(), Constants.NOTE_LIST_FRAGMENT)
                        .addToBackStack(null)
                        .commit();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        for (Fragment f : getSupportFragmentManager().getFragments()) {
            if (f.isVisible()) {
                FragmentManager childFm = f.getChildFragmentManager();
                if (childFm.getBackStackEntryCount() > 0) {
                    childFm.popBackStack();
                    return;
                }
            }
        }

        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            new ConfirmExitDialog().show(getSupportFragmentManager(), Constants.DIALOG_EXIT);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void update(Note note) {
        Bundle result = new Bundle();
        result.putSerializable(Constants.NOTE, note);
        getSupportFragmentManager().setFragmentResult(Constants.REQUEST_KEY, result);
    }

    @Override
    public void create(String title, String description, String importance, String date) {
        Bundle result = new Bundle();
        Note createdNote = new Note(-1, title, description, importance, date);
        result.putSerializable(Constants.NOTE, createdNote);
        getSupportFragmentManager().setFragmentResult(Constants.REQUEST_KEY, result);
    }
}


