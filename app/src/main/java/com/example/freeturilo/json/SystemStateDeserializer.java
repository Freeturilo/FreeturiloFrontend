package com.example.freeturilo.json;

import com.example.freeturilo.core.SystemState;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Custom deserializer for {@code SystemState}.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see JsonDeserializer
 * @see SystemState
 */
public class SystemStateDeserializer implements JsonDeserializer<SystemState> {

    /**
     * Deserializes json data containing an integer.
     * @param json      the json data being deserialized
     * @param typeOfT   the type of the object to deserialize to (the
     *                  {@code SystemState} class)
     * @param context   deserialization context
     * @return          a system state obtained from the json data
     * @throws JsonParseException   an exception representing an error caused
     *                              by an unexpected format of the json data
     */
    @Override
    public SystemState deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return SystemState.values()[json.getAsJsonPrimitive().getAsInt()];
    }
}
