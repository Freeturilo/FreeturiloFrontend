package com.example.freeturilo.json;

import static org.junit.Assert.assertEquals;

import com.example.freeturilo.core.Favourite;
import com.example.freeturilo.core.FavouriteType;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.Test;

public class FavouriteDeserializerTest {

    @Test
    public void deserialize() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("type", new JsonPrimitive("Favourite.SCHOOL"));
        jsonObject.add("name", new JsonPrimitive("MiNI"));
        jsonObject.add("latitude", new JsonPrimitive(52.221990));
        jsonObject.add("longitude", new JsonPrimitive(21.0070651));
        FavouriteDeserializer deserializer = new FavouriteDeserializer();

        Favourite favourite = deserializer.deserialize(jsonObject, null, null);

        assertEquals("MiNI", favourite.name);
        assertEquals(52.221990, favourite.latitude, 0.000001);
        assertEquals(21.0070651, favourite.longitude, 0.000001);
        assertEquals(FavouriteType.SCHOOL, favourite.type);
    }
}