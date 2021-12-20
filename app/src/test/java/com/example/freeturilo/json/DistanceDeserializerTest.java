package com.example.freeturilo.json;

import static org.junit.Assert.assertEquals;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.maps.model.Distance;

import org.junit.Test;

public class DistanceDeserializerTest {

    @Test
    public void deserialize() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("text", new JsonPrimitive("1km"));
        jsonObject.add("value", new JsonPrimitive(1000));
        DistanceDeserializer deserializer = new DistanceDeserializer();

        Distance distance = deserializer.deserialize(jsonObject, null, null);

        assertEquals("1km", distance.humanReadable);
        assertEquals(1000, distance.inMeters);
    }
}