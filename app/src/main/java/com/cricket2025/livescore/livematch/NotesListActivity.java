package com.cricket2025.livescore.livematch;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class NotesListActivity extends AppCompatActivity {

    private ListView listView;
    private TextView tvEmpty;
    private EditText etSearch;
    private NoteAdapter adapter;
    private NoteManager noteManager;
    private List<Note> allNotes;
    private List<Note> displayedNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        noteManager    = NoteManager.getInstance(this);
        listView       = findViewById(R.id.listViewNotes);
        tvEmpty        = findViewById(R.id.tvEmpty);
        etSearch       = findViewById(R.id.etSearch);
        Button btnBack    = findViewById(R.id.btnBack);
        Button btnAddNew  = findViewById(R.id.btnAddNew);

        btnBack.setOnClickListener(v -> finish());

        btnAddNew.setOnClickListener(v -> {
            Intent intent = new Intent(NotesListActivity.this, AddEditNoteActivity.class);
            intent.putExtra(AddEditNoteActivity.EXTRA_MODE, AddEditNoteActivity.MODE_ADD);
            startActivity(intent);
        });

        displayedNotes = new ArrayList<>();
        adapter = new NoteAdapter(this, displayedNotes,
                (note, position) -> {
                    noteManager.toggleFavorite(note.getId());
                    refreshList(etSearch.getText().toString());
                });
        listView.setAdapter(adapter);

        // Tap → Edit
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view,
                                              int position, long id) {
                Note note = displayedNotes.get(position);
                Intent intent = new Intent(NotesListActivity.this, AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_MODE,    AddEditNoteActivity.MODE_EDIT);
                intent.putExtra(AddEditNoteActivity.EXTRA_NOTE_ID, note.getId());
                startActivity(intent);
            }
        });

        // Long-press → Delete
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override public boolean onItemLongClick(AdapterView<?> parent, View view,
                                                     int position, long id) {
                final Note note = displayedNotes.get(position);
                new AlertDialog.Builder(NotesListActivity.this)
                        .setTitle(R.string.confirm_delete)
                        .setMessage("\"" + note.getDisplayTitle() + "\"")
                        .setPositiveButton(R.string.delete, (dialog, which) -> {
                            noteManager.deleteNote(note.getId());
                            Toast.makeText(NotesListActivity.this,
                                    R.string.note_deleted, Toast.LENGTH_SHORT).show();
                            refreshList(etSearch.getText().toString());
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                return true;
            }
        });

        // Search
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {
                refreshList(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList(etSearch.getText().toString());
    }

    private void refreshList(String query) {
        allNotes = noteManager.getAllNotes();
        displayedNotes.clear();

        if (query == null || query.trim().isEmpty()) {
            displayedNotes.addAll(allNotes);
        } else {
            String lower = query.toLowerCase().trim();
            for (Note n : allNotes) {
                boolean matchTitle   = n.getTitle() != null &&
                        n.getTitle().toLowerCase().contains(lower);
                boolean matchContent = n.getContent() != null &&
                        n.getContent().toLowerCase().contains(lower);
                if (matchTitle || matchContent) displayedNotes.add(n);
            }
        }

        adapter.notifyDataSetChanged();

        if (displayedNotes.isEmpty()) {
            listView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }
    }
}