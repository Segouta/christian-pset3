package com.example.christian.christian_pset3;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class MyAdapter extends ArrayAdapter<String> {

    public MyAdapter(@NonNull Context context, String[] values) {
        super(context, R.layout.row_layout, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater theInflator = LayoutInflater.from(getContext());

        View theView = theInflator.inflate(R.layout.row_layout, parent, false);

        String item = getItem(position);

        TextView textView = (TextView) theView.findViewById(R.id.entryText);

        textView.setText(item);

        return theView;

    }
}