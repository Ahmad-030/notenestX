package com.cricket2025.livescore.livematch;

public class Note {
    private String id;
    private String title;
    private String content;
    private boolean isFavorite;

    public Note() {}

    public Note(String id, String title, String content, boolean isFavorite) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isFavorite = isFavorite;
    }

    public String getId()            { return id; }
    public void setId(String id)     { this.id = id; }

    public String getTitle()              { return title; }
    public void setTitle(String title)    { this.title = title; }

    public String getContent()               { return content; }
    public void setContent(String content)   { this.content = content; }

    public boolean isFavorite()                    { return isFavorite; }
    public void setFavorite(boolean favorite)      { isFavorite = favorite; }

    /** Returns title if non-empty, else first line of content */
    public String getDisplayTitle() {
        if (title != null && !title.trim().isEmpty()) return title.trim();
        if (content != null && !content.trim().isEmpty()) {
            String first = content.trim().split("\n")[0];
            return first.length() > 40 ? first.substring(0, 40) + "…" : first;
        }
        return "(empty note)";
    }

    /** Returns content preview (first 80 chars) */
    public String getPreview() {
        if (content == null || content.trim().isEmpty()) return "(no content)";
        String c = content.trim();
        return c.length() > 80 ? c.substring(0, 80) + "…" : c;
    }
}