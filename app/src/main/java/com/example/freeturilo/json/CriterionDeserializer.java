package com.example.freeturilo.json;

import com.example.freeturilo.core.Criterion;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Custom deserializer for {@code Criterion}.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see JsonDeserializer
 * @see Criterion
 */
public class CriterionDeserializer implements JsonDeserializer<Criterion> {

    /**
     * Deserializes json data containing an integer.
     * @param json      the json data being deserialized
     * @param typeOfT   the type of the object to deserialize to (the
     *                  {@code Criterion} class)
     * @param context   deserialization context
     * @return          a criterion obtained from the json data
     * @throws JsonParseException   an exception representing an error caused
     *                              by an unexpected format of the json data
     */
    @Override
    public Criterion deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Criterion.values()[json.getAsJsonPrimitive().getAsInt()];
    }
}
