package com.example.freeturilo.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.freeturilo.core.Station;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.junit.Test;

public class StationSerializerTest {

    @Test
    public void serialize() {
        Station station = new Station("Metro Trocka", 52.275799, 21.056083, 2585942, 8, 14, 0);
        StationSerializer serializer = new StationSerializer();

        JsonElement element = serializer.serialize(station, null, null);

        assertTrue(element.isJsonObject());
        JsonObject jsonObject = element.getAsJsonObject();
        assertEquals("Metro Trocka", jsonObject.get("name").getAsString());
        assertEquals(52.275799, jsonObject.get("latitude").getAsDouble(), 0.000001);
        assertEquals(21.056083, jsonObject.get("longitude").getAsDouble(), 0.000001);
        assertEquals(2585942, jsonObject.get("id").getAsInt());
        assertEquals(8, jsonObject.get("bikeRacks").getAsInt());
        assertEquals(14, jsonObject.get("bikes").getAsInt());
        assertEquals(0, jsonObject.get("state").getAsInt());
        assertEquals("Station", jsonObject.get("type").getAsString());
    }
}