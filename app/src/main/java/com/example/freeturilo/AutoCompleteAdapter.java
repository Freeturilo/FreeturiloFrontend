package com.example.freeturilo;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.freeturilo.core.Location;

import java.util.List;

public class AutoCompleteAdapter extends ArrayAdapter<Location> {
    public AutoCompleteAdapter(@NonNull Context context, @NonNull List<Location> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    android.R.layout.simple_dropdown_item_1line, parent, false);
        }
        TextView text = convertView.findViewById(android.R.id.text1);
        text.setText(getItem(position).autoCompletePredictionText);
        return convertView;
    }
}
