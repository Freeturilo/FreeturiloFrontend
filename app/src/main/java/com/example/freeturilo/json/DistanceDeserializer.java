package com.example.freeturilo.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.maps.model.Distance;

import java.lang.reflect.Type;

public class DistanceDeserializer implements JsonDeserializer<Distance> {

    @Override
    public Distance deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        Distance distance = new Distance();
        distance.humanReadable = object.get("text").getAsString();
        distance.inMeters = object.get("value").getAsLong();
        return distance;
    }
}
