package com.example.tuan6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuan6.Activity.MainActivity;
import com.example.tuan6.Model.NotesModel;

import java.util.ArrayList;

public class NotesAdapter extends BaseAdapter {
    private MainActivity context;
    private int layout;
    private ArrayList<NotesModel> notesList;

    public NotesAdapter(MainActivity context, int layout, ArrayList<NotesModel> notesList) {
        this.context = context;
        this.layout = layout;
        this.notesList = notesList;
    }

    @Override
    public int getCount() {
        return notesList.size();
    }

    @Override
    public Object getItem(int position) {
        return notesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView textViewNameNote;
        ImageView imageViewEdit, imageViewDelete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);

            holder = new ViewHolder();
            holder.textViewNameNote = convertView.findViewById(R.id.textViewNameNote);
            holder.imageViewEdit = convertView.findViewById(R.id.imageViewEdit);
            holder.imageViewDelete = convertView.findViewById(R.id.imageViewDelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final NotesModel notes = notesList.get(position);
        holder.textViewNameNote.setText(notes.getNameNote());

        // Xử lý sự kiện bấm nút cập nhật
        holder.imageViewEdit.setOnClickListener(view -> {
            Toast.makeText(context, "Cập nhật " + notes.getNameNote(), Toast.LENGTH_SHORT).show();
            context.DialogCapNhatNotes(notes.getNameNote(), notes.getIdNote());
        });

        // Xử lý sự kiện bấm nút xóa
        holder.imageViewDelete.setOnClickListener(view -> {
            Toast.makeText(context, "Xóa " + notes.getNameNote(), Toast.LENGTH_SHORT).show();
            context.DialogXoaNotes(notes.getNameNote() ,notes.getIdNote());
        });

        return convertView;
    }

}
