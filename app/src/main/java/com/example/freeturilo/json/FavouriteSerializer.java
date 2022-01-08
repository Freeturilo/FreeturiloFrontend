package com.example.freeturilo.json;

import com.example.freeturilo.core.Favourite;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Custom serializer for {@code Favourite}.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #serialize
 * @see JsonSerializer
 * @see Favourite
 */
public class FavouriteSerializer implements JsonSerializer<Favourite> {

    /**
     * Serializes a {@code Favourite} to json data containing an object with
     * {@code String type}, {@code String name}, {@code double latitude} and
     * {@code double longitude} fields with the {@code type} equal to one of:
     * "Favourite.HOME", "Favourite.WORK", "Favourite.SCHOOL" and
     * "Favourite.OTHER".
     * @param src           the favourite being serialized
     * @param typeOfSrc     type of object to serialize (the {@code Favourite}
     *                      class)
     * @param context       serialization context
     * @return              json data representing the favourite
     */
    @Override
    public JsonElement serialize(Favourite src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("name", new JsonPrimitive(src.name));
        object.add("latitude", new JsonPrimitive(src.latitude));
        object.add("longitude", new JsonPrimitive(src.longitude));
        object.add("type", new JsonPrimitive(String.format("Favourite.%s", src.type.toString())));
        return object;
    }
}
