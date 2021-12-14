package com.example.freeturilo.json;

import com.example.freeturilo.core.Favourite;
import com.example.freeturilo.core.Location;
import com.example.freeturilo.core.Station;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class LocationDeserializer implements JsonDeserializer<Location> {

    @Override
    public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        String type = object.get("type").getAsString().split("\\.")[0];
        if (type.equals("Favourite"))
            return context.deserialize(json, Favourite.class);
        if (type.equals("Station"))
            return context.deserialize(json, Station.class);
        if (type.equals("Location"))
            return new Location(
                    object.get("name").getAsString(),
                    object.get("latitude").getAsDouble(),
                    object.get("longitude").getAsDouble()
            );
        throw new JsonParseException(String.format("Couldn't parse location type: %s.", type));
    }
}
