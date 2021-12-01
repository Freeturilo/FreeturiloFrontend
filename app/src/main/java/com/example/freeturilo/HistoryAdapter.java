package com.example.freeturilo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.freeturilo.core.CriterionTools;
import com.example.freeturilo.core.RouteParameters;

import java.util.List;

public class HistoryAdapter extends ArrayAdapter<RouteParameters> {
    public HistoryAdapter(@NonNull Context context, @NonNull List<RouteParameters> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_history, parent, false);
        }
        RouteParameters routeParameters = getItem(position);
        TextView startText = convertView.findViewById(R.id.start_text);
        startText.setText(routeParameters.start.name);
        TextView endText = convertView.findViewById(R.id.end_text);
        endText.setText(routeParameters.end.name);
        TextView criterionText = convertView.findViewById(R.id.criterion_text);
        criterionText.setText(CriterionTools.getCriterionText(getContext(), routeParameters.criterion));
        return convertView;
    }

}
