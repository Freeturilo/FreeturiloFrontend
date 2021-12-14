package com.example.freeturilo.json;

import com.example.freeturilo.core.SystemState;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class SystemStateDeserializer implements JsonDeserializer<SystemState> {

    @Override
    public SystemState deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return SystemState.values()[json.getAsJsonPrimitive().getAsInt()];
    }
}
