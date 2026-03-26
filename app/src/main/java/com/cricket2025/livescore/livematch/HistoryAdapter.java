package com.cricket2025.livescore.livematch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class HistoryAdapter extends ArrayAdapter<Note> {

    public HistoryAdapter(Context context, List<Note> notes) {
        super(context, 0, notes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_history, parent, false);
            holder = new ViewHolder();
            holder.tvIndex   = convertView.findViewById(R.id.tvIndex);
            holder.tvTitle   = convertView.findViewById(R.id.tvHistoryTitle);
            holder.tvPreview = convertView.findViewById(R.id.tvHistoryPreview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Note note = getItem(position);
        if (note == null) return convertView;

        holder.tvIndex.setText(String.valueOf(position + 1));

        String title = note.getTitle();
        if (title != null && !title.trim().isEmpty()) {
            holder.tvTitle.setVisibility(View.VISIBLE);
            holder.tvTitle.setText(title.trim());
        } else {
            holder.tvTitle.setVisibility(View.GONE);
        }

        holder.tvPreview.setText(note.getPreview());
        return convertView;
    }

    static class ViewHolder {
        TextView tvIndex, tvTitle, tvPreview;
    }
}