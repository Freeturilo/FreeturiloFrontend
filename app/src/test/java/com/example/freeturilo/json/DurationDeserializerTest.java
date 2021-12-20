package com.example.freeturilo.json;

import static org.junit.Assert.assertEquals;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.maps.model.Duration;

import org.junit.Test;

public class DurationDeserializerTest {

    @Test
    public void deserialize() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("text", new JsonPrimitive("14min"));
        jsonObject.add("value", new JsonPrimitive(864));
        DurationDeserializer deserializer = new DurationDeserializer();

        Duration duration = deserializer.deserialize(jsonObject, null, null);

        assertEquals("14min", duration.humanReadable);
        assertEquals(864, duration.inSeconds);
    }
}