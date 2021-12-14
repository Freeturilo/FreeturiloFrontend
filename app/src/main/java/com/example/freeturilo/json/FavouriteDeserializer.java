package com.example.freeturilo.json;

import com.example.freeturilo.core.Favourite;
import com.example.freeturilo.core.FavouriteType;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class FavouriteDeserializer implements JsonDeserializer<Favourite> {

    @Override
    public Favourite deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        FavouriteType favouriteType = FavouriteType.valueOf(object.get("type").getAsString().split("\\.")[1]);
        return new Favourite(
                object.get("name").getAsString(),
                object.get("latitude").getAsDouble(),
                object.get("longitude").getAsDouble(),
                favouriteType);
    }
}
