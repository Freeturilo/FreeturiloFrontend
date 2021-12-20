package com.example.freeturilo.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.freeturilo.core.Location;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.junit.Test;

public class LocationSerializerTest {

    @Test
    public void serialize() {
        Location location = new Location("Westfield Arkadia", 52.256128, 20.985926);
        LocationSerializer<Location> locationSerializer = new LocationSerializer<>();

        JsonElement element = locationSerializer.serialize(location, null, null);

        assertTrue(element.isJsonObject());
        JsonObject jsonObject = element.getAsJsonObject();
        assertEquals("Westfield Arkadia", jsonObject.get("name").getAsString());
        assertEquals(52.256128, jsonObject.get("latitude").getAsDouble(), 0.000001);
        assertEquals(20.985926, jsonObject.get("longitude").getAsDouble(), 0.000001);
        assertEquals("Location", jsonObject.get("type").getAsString());
    }
}