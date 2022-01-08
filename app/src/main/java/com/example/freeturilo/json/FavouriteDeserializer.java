package com.example.freeturilo.json;

import com.example.freeturilo.core.Favourite;
import com.example.freeturilo.core.FavouriteType;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Custom deserializer for {@code Favourite}.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #deserialize
 * @see JsonDeserializer
 * @see Favourite
 */
public class FavouriteDeserializer implements JsonDeserializer<Favourite> {

    /**
     * Deserializes json data containing an object with {@code String type},
     * {@code String name}, {@code double latitude} and {@code double
     * longitude} fields with the {@code type} equal to one of: "Favourite.HOME",
     * "Favourite.WORK", "Favourite.SCHOOL" and "Favourite.OTHER".
     * @param json      the json data being deserialized
     * @param typeOfT   the type of the object to deserialize to (the
     *                  {@code Favourite} class)
     * @param context   deserialization context
     * @return          a favourite obtained from the json data
     * @throws JsonParseException   an exception representing an error caused
     *                              by an unexpected format of the json data
     */
    @Override
    public Favourite deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        FavouriteType favouriteType = FavouriteType.valueOf(object.get("type").getAsString().split("\\.")[1]);
        return new Favourite(
                object.get("name").getAsString(),
                object.get("latitude").getAsDouble(),
                object.get("longitude").getAsDouble(),
                favouriteType);
    }
}
