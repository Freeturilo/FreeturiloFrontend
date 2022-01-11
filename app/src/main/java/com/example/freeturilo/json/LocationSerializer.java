package com.example.freeturilo.json;

import com.example.freeturilo.core.Location;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Custom serializer for {@code Location}.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see JsonSerializer
 * @see Location
 */
public class LocationSerializer<T extends Location> implements JsonSerializer<T> {

    /**
     * Serializes a {@code Location} to json data containing an object with
     * {@code String type}, {@code String name}, {@code double latitude} and
     * {@code double longitude} fields with the {@code type} equal to
     * "Location".
     * @param src           the location being serialized
     * @param typeOfSrc     type of object to serialize (the {@code Location}
     *                      class)
     * @param context       serialization context
     * @return              json data representing the location
     */
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
