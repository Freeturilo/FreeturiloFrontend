package com.example.freeturilo.json;

import com.example.freeturilo.core.Favourite;
import com.example.freeturilo.core.Location;
import com.example.freeturilo.core.Station;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Custom deserializer for {@code Location}.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see JsonDeserializer
 * @see Location
 */
public class LocationDeserializer implements JsonDeserializer<Location> {

    /**
     * Deserializes json data containing an object with {@code type},
     * {@code name}, {@code latitude} and {@code longitude} fields with the
     * {@code type} equal to one of: "Location", "Station", "Favourite.HOME",
     * "Favourite.WORK", "Favourite.SCHOOL" and "Favourite.OTHER". This
     * deserializer performs full {@code Favourite} deserialization or full
     * {@code Station} deserialization if {@code type} doesn't equal to
     * Location.
     * @param json      the json data being deserialized
     * @param typeOfT   the type of the object to deserialize to (the
     *                  {@code Location} class)
     * @param context   deserialization context
     * @return          a location obtained from the json data
     * @throws JsonParseException   an exception representing an error caused
     *                              by an unexpected format of the json data
     */
    @Override
    public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        String type = object.get("type").getAsString().split("\\.")[0];
        if (type.equals("Favourite"))
            return context.deserialize(json, Favourite.class);
        if (type.equals("Station"))
            return context.deserialize(json, Station.class);
        if (type.equals("Location"))
            return new Location(
                    object.get("name").getAsString(),
                    object.get("latitude").getAsDouble(),
                    object.get("longitude").getAsDouble()
            );
        throw new JsonParseException(String.format("Couldn't parse location type: %s.", type));
    }
}
