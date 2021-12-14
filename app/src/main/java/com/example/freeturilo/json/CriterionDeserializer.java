package com.example.freeturilo.json;

import com.example.freeturilo.core.Criterion;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class CriterionDeserializer implements JsonDeserializer<Criterion> {

    @Override
    public Criterion deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Criterion.values()[json.getAsJsonPrimitive().getAsInt()];
    }
}
