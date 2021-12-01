package com.example.freeturilo;

import android.content.Context;
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
                    R.layout.item_autocomplete, parent, false);
        }
        Location location = getItem(position);
        TextView textPrimary = convertView.findViewById(R.id.text_primary);
        textPrimary.setText(location.getPrimaryText());
        TextView textSecondary = convertView.findViewById(R.id.text_secondary);
        textSecondary.setText(String.format(", %s", location.getSecondaryText(getContext())));
        return convertView;
    }
}
