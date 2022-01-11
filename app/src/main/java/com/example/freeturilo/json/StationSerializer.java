package com.example.freeturilo.json;

import com.example.freeturilo.core.Station;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Custom serializer for {@code Station}.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see JsonSerializer
 * @see Station
 */
public class StationSerializer implements JsonSerializer<Station> {

    /**
     * Serializes a {@code Station} to json data containing an object with
     * {@code String type}, {@code String name}, {@code double latitude},
     * {@code double longitude}, {@code int id}, {@code int bikeRacks},
     * {@code int bikes} and {@code int state}, fields with the {@code type}
     * equal to "Station".
     * @param src           the station being serialized
     * @param typeOfSrc     type of object to serialize (the {@code Station}
     *                      class)
     * @param context       serialization context
     * @return              json data representing the station
     */
    @Override
    public JsonElement serialize(Station src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("name", new JsonPrimitive(src.name));
        object.add("latitude", new JsonPrimitive(src.latitude));
        object.add("longitude", new JsonPrimitive(src.longitude));
        object.add("id", new JsonPrimitive(src.id));
        object.add("bikeRacks", new JsonPrimitive(src.bikeRacks));
        object.add("bikes", new JsonPrimitive(src.bikes));
        object.add("state", new JsonPrimitive(src.state));
        object.add("type", new JsonPrimitive("Station"));
        return object;
    }
}
