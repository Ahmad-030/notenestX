package com.cricket2025.livescore.livematch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {

    private final NoteManager noteManager;
    private final OnFavoriteToggleListener favoriteListener;

    public interface OnFavoriteToggleListener {
        void onFavoriteToggled(Note note, int position);
    }

    public NoteAdapter(Context context, List<Note> notes,
                       OnFavoriteToggleListener listener) {
        super(context, 0, notes);
        noteManager      = NoteManager.getInstance(context);
        favoriteListener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_note, parent, false);
            holder = new ViewHolder();
            holder.tvTitle   = convertView.findViewById(R.id.tvNoteTitle);
            holder.tvPreview = convertView.findViewById(R.id.tvNotePreview);
            holder.tvStar    = convertView.findViewById(R.id.tvStar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Note note = getItem(position);
        if (note == null) return convertView;

        // Title
        String title = note.getTitle();
        if (title != null && !title.trim().isEmpty()) {
            holder.tvTitle.setVisibility(View.VISIBLE);
            holder.tvTitle.setText(title.trim());
        } else {
            holder.tvTitle.setVisibility(View.GONE);
        }

        holder.tvPreview.setText(note.getPreview());

        // Star
        holder.tvStar.setText(note.isFavorite() ? "★" : "☆");
        holder.tvStar.setTextColor(note.isFavorite()
                ? getContext().getResources().getColor(R.color.star_active)
                : getContext().getResources().getColor(R.color.star_inactive));

        // Card background
        convertView.setBackground(note.isFavorite()
                ? getContext().getResources().getDrawable(R.drawable.card_note_favorite)
                : getContext().getResources().getDrawable(R.drawable.card_note));

        holder.tvStar.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (favoriteListener != null) {
                    favoriteListener.onFavoriteToggled(note, position);
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView tvTitle, tvPreview, tvStar;
    }
}