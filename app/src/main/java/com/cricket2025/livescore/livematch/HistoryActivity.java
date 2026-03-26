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

public class HistoryActivity extends AppCompatActivity {

    private ListView listView;
    private TextView tvEmpty;
    private HistoryAdapter adapter;
    private NoteManager noteManager;
    private List<Note> historyNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        noteManager  = NoteManager.getInstance(this);
        listView     = findViewById(R.id.listViewHistory);
        tvEmpty      = findViewById(R.id.tvEmpty);
        Button btnBack         = findViewById(R.id.btnBack);
        Button btnClearHistory = findViewById(R.id.btnClearHistory);

        btnBack.setOnClickListener(v -> finish());

        btnClearHistory.setOnClickListener(v -> {
            if (historyNotes.isEmpty()) {
                Toast.makeText(this, getString(R.string.empty_history), Toast.LENGTH_SHORT).show();
                return;
            }
            new AlertDialog.Builder(HistoryActivity.this)
                    .setTitle(R.string.confirm_clear_history)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        noteManager.clearHistory();
                        Toast.makeText(HistoryActivity.this,
                                R.string.history_cleared, Toast.LENGTH_SHORT).show();
                        refreshList();
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        });

        historyNotes = new ArrayList<>();
        adapter      = new HistoryAdapter(this, historyNotes);
        listView.setAdapter(adapter);

        // Tap → Edit
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view,
                                              int position, long id) {
                Note note = historyNotes.get(position);
                Intent intent = new Intent(HistoryActivity.this, AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_MODE,    AddEditNoteActivity.MODE_EDIT);
                intent.putExtra(AddEditNoteActivity.EXTRA_NOTE_ID, note.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        historyNotes.clear();
        historyNotes.addAll(noteManager.getHistory());
        adapter.notifyDataSetChanged();

        if (historyNotes.isEmpty()) {
            listView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }
    }
}