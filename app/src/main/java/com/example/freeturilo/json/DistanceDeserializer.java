package com.example.freeturilo.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.maps.model.Distance;

import java.lang.reflect.Type;

/**
 * Custom deserializer for {@code Distance}.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see JsonDeserializer
 * @see Distance
 */
public class DistanceDeserializer implements JsonDeserializer<Distance> {

    /**
     * Deserializes json data containing an object with {@code text} and
     * {@code value} fields containing respectively a {@code humanReadable} and
     * an {@code inMeters} value of a {@code Distance}.
     * @param json      the json data being deserialized
     * @param typeOfT   the type of the object to deserialize to (the
     *                  {@code Distance} class)
     * @param context   deserialization context
     * @return          a distance obtained from the json data
     * @throws JsonParseException   an exception representing an error caused
     *                              by an unexpected format of the json data
     */
    @Override
    public Distance deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        Distance distance = new Distance();
        distance.humanReadable = object.get("text").getAsString();
        distance.inMeters = object.get("value").getAsLong();
        return distance;
    }
}
