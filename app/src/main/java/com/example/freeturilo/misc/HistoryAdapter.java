package com.example.freeturilo.misc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freeturilo.R;
import com.example.freeturilo.activities.HistoryActivity;
import com.example.freeturilo.core.Criterion;
import com.example.freeturilo.core.RouteParameters;

import java.util.List;

public class HistoryAdapter extends ArrayAdapter<RouteParameters> {

    public HistoryAdapter(@NonNull Context context, @NonNull List<RouteParameters> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_history, parent, false);
        RouteParameters routeParameters = getItem(position);
        TextView startText = convertView.findViewById(R.id.start_text);
        startText.setText(routeParameters.start.name);
        TextView endText = convertView.findViewById(R.id.end_text);
        endText.setText(routeParameters.end.name);
        TextView criterionText = convertView.findViewById(R.id.criterion_text);
        criterionText.setText(Criterion.getCriterionText(getContext(), routeParameters.criterion));
        ImageButton deleteItemButton = convertView.findViewById(R.id.delete_history_item_button);
        if (getContext() instanceof HistoryActivity) {
            HistoryActivity activity = (HistoryActivity) getContext();
            deleteItemButton.setOnClickListener((view) -> activity.deleteHistoryItem(routeParameters));
        }
        return convertView;
    }

}
