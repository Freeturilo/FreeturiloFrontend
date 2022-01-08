package com.example.freeturilo.json;

import com.example.freeturilo.core.Criterion;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Custom serializer for {@code Criterion}.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #serialize
 * @see JsonSerializer
 * @see Criterion
 */
public class CriterionSerializer implements JsonSerializer<Criterion> {

    /**
     * Serializes a criterion to json data containing an integer.
     * @param src           the criterion being serialized
     * @param typeOfSrc     type of object to serialize (the {@code Criterion}
     *                      class)
     * @param context       serialization context
     * @return              json data representing the criterion
     */
    @Override
    public JsonElement serialize(Criterion src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.ordinal());
    }
}
