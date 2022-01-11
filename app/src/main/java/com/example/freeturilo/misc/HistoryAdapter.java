package com.example.freeturilo.misc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freeturilo.R;
import com.example.freeturilo.activities.HistoryActivity;
import com.example.freeturilo.core.Criterion;
import com.example.freeturilo.core.Location;
import com.example.freeturilo.core.RouteParameters;

import java.util.List;

/**
 * An adapter converting route history items to views.
 * <p>
 * An object of this class is used as an adapter in the {@code HistoryActivity}.
 * It populates a {@code ListView} with views containing detailed information
 * about parameters used to calculate routes in the past.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see ArrayAdapter
 * @see RouteParameters
 * @see ListView
 */
public class HistoryAdapter extends ArrayAdapter<RouteParameters> {

    /**
     * Class constructor.
     * @param context   the context of the application providing all global
     *                  information
     * @param objects   a list of history items to be shown in a listview
     */
    public HistoryAdapter(@NonNull Context context, @NonNull List<RouteParameters> objects) {
        super(context, 0, objects);
    }

    /**
     * Gets a view representing a history item.
     * @param position      an integer equal to the position of the history
     *                      item in this adapter's data set
     * @param convertView   a view generated before for the history item
     *                      or null if the view has not been generated before
     * @param parent        the parent that this view will eventually be
     *                      attached to
     * @return              a view that represents the history items
     * @see #getItem
     */
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
