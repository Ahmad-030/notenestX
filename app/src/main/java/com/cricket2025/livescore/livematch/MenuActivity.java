package com.cricket2025.livescore.livematch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button btnNewNote   = findViewById(R.id.btnNewNote);
        Button btnViewNotes = findViewById(R.id.btnViewNotes);
        Button btnFavorites = findViewById(R.id.btnFavorites);
        Button btnHistory   = findViewById(R.id.btnHistory);
        Button btnTips      = findViewById(R.id.btnTips);
        Button btnAbout     = findViewById(R.id.btnAbout);
        Button btnExit      = findViewById(R.id.btnExit);

        btnNewNote.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_MODE, AddEditNoteActivity.MODE_ADD);
                startActivity(intent);
            }
        });

        btnViewNotes.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, NotesListActivity.class));
            }
        });

        btnFavorites.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, FavoritesActivity.class));
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, HistoryActivity.class));
            }
        });

        btnTips.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, TipsActivity.class));
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, AboutActivity.class));
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                new AlertDialog.Builder(MenuActivity.this)
                        .setTitle("Exit NoteNestX")
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Exit", (dialog, which) -> finishAffinity())
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
    }
}