package com.example.freeturilo.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.maps.model.Duration;

import java.lang.reflect.Type;

/**
 * Custom deserializer for {@code Duration}.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #deserialize
 * @see JsonDeserializer
 * @see Duration
 */
public class DurationDeserializer implements JsonDeserializer<Duration> {

    /**
     * Deserializes json data containing an object with {@code text} and
     * {@code value} fields containing respectively a {@code humanReadable} and
     * an {@code inSeconds} value of a {@code Duration}.
     * @param json      the json data being deserialized
     * @param typeOfT   the type of the object to deserialize to (the
     *                  {@code Duration} class)
     * @param context   deserialization context
     * @return          a duration obtained from the json data
     * @throws JsonParseException   an exception representing an error caused
     *                              by an unexpected format of the json data
     */
    @Override
    public Duration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        Duration duration = new Duration();
        duration.humanReadable = object.get("text").getAsString();
        duration.inSeconds = object.get("value").getAsLong();
        return duration;
    }
}
