package com.cricket2025.livescore.livematch;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_MODE    = "mode";
    public static final String EXTRA_NOTE_ID = "note_id";
    public static final int    MODE_ADD      = 0;
    public static final int    MODE_EDIT     = 1;

    private EditText etTitle, etContent;
    private NoteManager noteManager;
    private int mode;
    private String noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        noteManager = NoteManager.getInstance(this);

        etTitle   = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etNoteContent);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnBack = findViewById(R.id.btnBack);
        TextView tvScreenTitle = findViewById(R.id.tvScreenTitle);

        mode   = getIntent().getIntExtra(EXTRA_MODE, MODE_ADD);
        noteId = getIntent().getStringExtra(EXTRA_NOTE_ID);

        if (mode == MODE_EDIT && noteId != null) {
            tvScreenTitle.setText(R.string.label_edit_note);
            Note existing = noteManager.getNoteById(noteId);
            if (existing != null) {
                etTitle.setText(existing.getTitle());
                etContent.setText(existing.getContent());
            }
        } else {
            tvScreenTitle.setText(R.string.label_add_note);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                saveNote();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveNote() {
        String title   = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, getString(R.string.no_content), Toast.LENGTH_SHORT).show();
            return;
        }

        if (mode == MODE_EDIT && noteId != null) {
            Note updated = noteManager.getNoteById(noteId);
            if (updated != null) {
                updated.setTitle(title);
                updated.setContent(content);
                noteManager.updateNote(updated);
            }
        } else {
            Note note = new Note();
            note.setTitle(title);
            note.setContent(content);
            note.setFavorite(false);
            noteManager.addNote(note);
        }

        Toast.makeText(this, getString(R.string.note_saved), Toast.LENGTH_SHORT).show();
        finish();
    }
}