package com.example.freeturilo.json;

import com.example.freeturilo.core.Location;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class LocationSerializer<T extends Location> implements JsonSerializer<T> {

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("name", new JsonPrimitive(src.name));
        object.add("latitude", new JsonPrimitive(src.latitude));
        object.add("longitude", new JsonPrimitive(src.longitude));
        object.add("type", new JsonPrimitive("Location"));
        return object;
    }
}
