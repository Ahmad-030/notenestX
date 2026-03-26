package com.cricket2025.livescore.livematch;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton data manager. All persistence via SharedPreferences + JSON.
 *
 * Keys:
 *   "notes_list"   – JSON array of note objects
 *   "history_list" – JSON array of note ids (max 5, newest first)
 */
public class NoteManager {

    private static final String PREFS_NAME    = "NoteNestXPrefs";
    private static final String KEY_NOTES     = "notes_list";
    private static final String KEY_HISTORY   = "history_list";
    private static final int    MAX_HISTORY   = 5;

    private static NoteManager instance;
    private final SharedPreferences prefs;

    private NoteManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static NoteManager getInstance(Context context) {
        if (instance == null) instance = new NoteManager(context);
        return instance;
    }

    // ------------------------------------------------------------------ NOTES

    /** Load all notes from SharedPreferences */
    public List<Note> getAllNotes() {
        List<Note> list = new ArrayList<>();
        String json = prefs.getString(KEY_NOTES, "[]");
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                list.add(noteFromJson(arr.getJSONObject(i)));
            }
        } catch (JSONException e) { e.printStackTrace(); }
        return list;
    }

    /** Save entire notes list */
    private void saveAllNotes(List<Note> notes) {
        JSONArray arr = new JSONArray();
        for (Note n : notes) arr.put(noteToJson(n));
        prefs.edit().putString(KEY_NOTES, arr.toString()).apply();
    }

    /** Add a new note; also pushes to history */
    public void addNote(Note note) {
        List<Note> notes = getAllNotes();
        note.setId(generateId());
        notes.add(0, note);          // newest first
        saveAllNotes(notes);
        pushHistory(note.getId());
    }

    /** Update an existing note by id; also pushes to history */
    public void updateNote(Note updated) {
        List<Note> notes = getAllNotes();
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId().equals(updated.getId())) {
                notes.set(i, updated);
                break;
            }
        }
        saveAllNotes(notes);
        pushHistory(updated.getId());
    }

    /** Delete note by id; also removes from history */
    public void deleteNote(String id) {
        List<Note> notes = getAllNotes();
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId().equals(id)) {
                notes.remove(i);
                break;
            }
        }
        saveAllNotes(notes);
        removeFromHistory(id);
    }

    /** Find note by id; returns null if not found */
    public Note getNoteById(String id) {
        for (Note n : getAllNotes()) {
            if (n.getId().equals(id)) return n;
        }
        return null;
    }

    /** Toggle favorite flag */
    public void toggleFavorite(String id) {
        List<Note> notes = getAllNotes();
        for (Note n : notes) {
            if (n.getId().equals(id)) {
                n.setFavorite(!n.isFavorite());
                break;
            }
        }
        saveAllNotes(notes);
    }

    /** Returns only favorite notes */
    public List<Note> getFavorites() {
        List<Note> favs = new ArrayList<>();
        for (Note n : getAllNotes()) {
            if (n.isFavorite()) favs.add(n);
        }
        return favs;
    }

    // --------------------------------------------------------------- HISTORY

    /** Returns ordered list of recent notes (max 5, newest first) */
    public List<Note> getHistory() {
        List<Note> result = new ArrayList<>();
        List<String> ids = getHistoryIds();
        for (String id : ids) {
            Note n = getNoteById(id);
            if (n != null) result.add(n);
        }
        return result;
    }

    /** Push a note id to history (removes duplicates, trims to MAX_HISTORY) */
    private void pushHistory(String id) {
        List<String> ids = getHistoryIds();
        ids.remove(id);              // remove if already present
        ids.add(0, id);              // add to front
        if (ids.size() > MAX_HISTORY) ids = ids.subList(0, MAX_HISTORY);
        saveHistoryIds(ids);
    }

    private void removeFromHistory(String id) {
        List<String> ids = getHistoryIds();
        ids.remove(id);
        saveHistoryIds(ids);
    }

    public void clearHistory() {
        prefs.edit().putString(KEY_HISTORY, "[]").apply();
    }

    private List<String> getHistoryIds() {
        List<String> ids = new ArrayList<>();
        String json = prefs.getString(KEY_HISTORY, "[]");
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) ids.add(arr.getString(i));
        } catch (JSONException e) { e.printStackTrace(); }
        return ids;
    }

    private void saveHistoryIds(List<String> ids) {
        JSONArray arr = new JSONArray();
        for (String id : ids) arr.put(id);
        prefs.edit().putString(KEY_HISTORY, arr.toString()).apply();
    }

    // ---------------------------------------------------------------- HELPERS

    private String generateId() {
        return "note_" + System.currentTimeMillis();
    }

    private Note noteFromJson(JSONObject obj) throws JSONException {
        return new Note(
                obj.getString("id"),
                obj.optString("title", ""),
                obj.optString("content", ""),
                obj.optBoolean("isFavorite", false)
        );
    }

    private JSONObject noteToJson(Note n) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id",         n.getId());
            obj.put("title",      n.getTitle() != null ? n.getTitle() : "");
            obj.put("content",    n.getContent() != null ? n.getContent() : "");
            obj.put("isFavorite", n.isFavorite());
        } catch (JSONException e) { e.printStackTrace(); }
        return obj;
    }
}