package com.example.freeturilo.misc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freeturilo.R;
import com.example.freeturilo.core.Location;

import java.util.List;

/**
 * An adapter converting autocomplete suggestions to views.
 * <p>
 * An object of this class is used as an adapter in an
 * {@code AutoCompleteTextView} used for selecting locations. It populates
 * the dropdown of the textview with views containing detailed information
 * about suggested locations.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see ArrayAdapter
 * @see Location
 * @see AutoCompleteTextView
 */
public class AutoCompleteAdapter extends ArrayAdapter<Location> {

    /**
     * Class constructor.
     * @param context   the context of the application providing all global
     *                  information
     * @param objects   a list of suggested locations to be shown in a dropdown
     */
    public AutoCompleteAdapter(@NonNull Context context, @NonNull List<Location> objects) {
        super(context, 0, objects);
    }

    /**
     * Gets a view representing a suggested location.
     * @param position      an integer equal to the position of the suggested
     *                      location in this adapter's data set
     * @param convertView   a view generated before for the suggested location
     *                      or null if the view has not been generated before
     * @param parent        the parent that this view will eventually be
     *                      attached to
     * @return              a view that represents the suggested location
     * @see #getItem
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_autocomplete, parent, false);
        Location location = getItem(position);
        TextView textPrimary = convertView.findViewById(R.id.text_primary);
        textPrimary.setText(location.getPrimaryText());
        TextView textSecondary = convertView.findViewById(R.id.text_secondary);
        textSecondary.setText(location.getInlineSecondaryText(getContext()));
        return convertView;
    }
}
