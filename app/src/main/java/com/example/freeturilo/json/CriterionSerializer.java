package com.example.freeturilo.json;

import com.example.freeturilo.core.Criterion;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class CriterionSerializer implements JsonSerializer<Criterion> {

    @Override
    public JsonElement serialize(Criterion src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.ordinal());
    }
}
