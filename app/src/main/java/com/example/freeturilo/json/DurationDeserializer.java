package com.example.freeturilo.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.maps.model.Duration;

import java.lang.reflect.Type;

public class DurationDeserializer implements JsonDeserializer<Duration> {

    @Override
    public Duration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        Duration duration = new Duration();
        duration.humanReadable = object.get("text").getAsString();
        duration.inSeconds = object.get("value").getAsLong();
        return duration;
    }
}
