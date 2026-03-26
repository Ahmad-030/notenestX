package com.cricket2025.livescore.livematch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private ListView listView;
    private TextView tvEmpty;
    private NoteAdapter adapter;
    private NoteManager noteManager;
    private List<Note> favNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        noteManager = NoteManager.getInstance(this);
        listView    = findViewById(R.id.listViewFavorites);
        tvEmpty     = findViewById(R.id.tvEmpty);
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        favNotes = new ArrayList<>();
        adapter  = new NoteAdapter(this, favNotes,
                (note, position) -> {
                    noteManager.toggleFavorite(note.getId());
                    refreshList();
                });
        listView.setAdapter(adapter);

        // Tap → Edit
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view,
                                              int position, long id) {
                Note note = favNotes.get(position);
                Intent intent = new Intent(FavoritesActivity.this, AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_MODE,    AddEditNoteActivity.MODE_EDIT);
                intent.putExtra(AddEditNoteActivity.EXTRA_NOTE_ID, note.getId());
                startActivity(intent);
            }
        });

        // Long-press → Delete
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override public boolean onItemLongClick(AdapterView<?> parent, View view,
                                                     int position, long id) {
                final Note note = favNotes.get(position);
                new AlertDialog.Builder(FavoritesActivity.this)
                        .setTitle(R.string.confirm_delete)
                        .setMessage("\"" + note.getDisplayTitle() + "\"")
                        .setPositiveButton(R.string.delete, (dialog, which) -> {
                            noteManager.deleteNote(note.getId());
                            Toast.makeText(FavoritesActivity.this,
                                    R.string.note_deleted, Toast.LENGTH_SHORT).show();
                            refreshList();
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        favNotes.clear();
        favNotes.addAll(noteManager.getFavorites());
        adapter.notifyDataSetChanged();

        if (favNotes.isEmpty()) {
            listView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }
    }
}