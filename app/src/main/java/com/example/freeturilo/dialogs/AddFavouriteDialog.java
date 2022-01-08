package com.example.freeturilo.dialogs;

import androidx.annotation.NonNull;

import com.example.freeturilo.R;
import com.example.freeturilo.core.Favourite;
import com.example.freeturilo.core.FavouriteType;
import com.example.freeturilo.misc.Callback;
import com.google.android.gms.maps.model.LatLng;

/**
 * A dialog for adding favourite locations.
 * <p>
 * Object of this class is used to create a {@code Favourite} location with
 * custom parameters.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see FavouriteDialog
 */
public class AddFavouriteDialog extends FavouriteDialog {

    /**
     * Class constructor.
     * @param latLng            coordinates of the created favourite location
     * @param positiveCallback  a callback which is called after this dialogs
     *                          finishes with a positive button click
     */
    public AddFavouriteDialog(@NonNull LatLng latLng, @NonNull Callback<Favourite> positiveCallback) {
        super(new Favourite("none", latLng.latitude, latLng.longitude, FavouriteType.OTHER), positiveCallback);
        this.titleResourceId = R.string.add_favourite_dialog_title;
        this.positiveTextResourceId = R.string.ok_text;
        this.negativeTextResourceId = R.string.cancel_text;
    }
}
