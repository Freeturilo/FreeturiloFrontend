package com.example.freeturilo.json;

import com.example.freeturilo.core.Station;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class StationSerializer implements JsonSerializer<Station> {

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
