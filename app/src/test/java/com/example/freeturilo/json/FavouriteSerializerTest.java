package com.example.freeturilo.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.freeturilo.core.Favourite;
import com.example.freeturilo.core.FavouriteType;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.junit.Test;

public class FavouriteSerializerTest {

    @Test
    public void serialize() {
        Favourite favourite = new Favourite("Mieszkanie", 52.2468349, 20.9981223, FavouriteType.HOME);
        FavouriteSerializer serializer = new FavouriteSerializer();

        JsonElement element = serializer.serialize(favourite, null, null);

        assertTrue(element.isJsonObject());
        JsonObject jsonObject = element.getAsJsonObject();
        assertEquals("Mieszkanie", jsonObject.get("name").getAsString());
        assertEquals(52.2468349, jsonObject.get("latitude").getAsDouble(), 0.000001);
        assertEquals(20.9981223, jsonObject.get("longitude").getAsDouble(), 0.000001);
        assertEquals("Favourite.HOME", jsonObject.get("type").getAsString());
    }
}