package com.example.cloudbasednoteservice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class NoteAdapter extends BaseAdapter {

    private Context context;
    private List<Note> notes;

    public NoteAdapter(Context context, List<Note> items) {
        this.context = context;
        this.notes = items;
    }

    @Override
    public int getCount() {
        return this.notes.size();
    }

    public Object getItem(int position) {
        return this.notes.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View
    getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(R.layout.item, parent, false);
        }

        Note note = (Note) getItem(position);

        TextView name = (TextView) convertView.findViewById(R.id.content);

        name.setText(note.getContent());

        return convertView;
    }
}

