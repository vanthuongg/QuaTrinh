package com.example.customadapter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.customadapter.R;
import com.example.customadapter.models.MonHoc;

import java.util.List;

public class MonHocAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<MonHoc> listMonHoc;

    public MonHocAdapter(Context context, int layout, List<MonHoc> listMonHoc) {
        this.context = context;
        this.layout = layout;
        this.listMonHoc = listMonHoc;
    }

    @Override
    public int getCount() {
        return listMonHoc.size();
    }

    @Override
    public Object getItem(int position) {
        return listMonHoc.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout, null);

        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtDesc = convertView.findViewById(R.id.txtDes);
        ImageView image = convertView.findViewById(R.id.imgHinh);

        MonHoc monHoc = listMonHoc.get(position);
        txtName.setText(monHoc.getName());
        txtDesc.setText(monHoc.getDescr());
        image.setImageResource(monHoc.getImage());

        return convertView;
    }


}
