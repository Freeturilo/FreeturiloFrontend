package com.example.freeturilo.json;

import static org.junit.Assert.assertEquals;

import com.example.freeturilo.core.Favourite;
import com.example.freeturilo.core.FavouriteType;
import com.example.freeturilo.core.Location;
import com.example.freeturilo.core.Station;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import org.junit.Test;

import java.lang.reflect.Type;

public class LocationDeserializerTest {

    private static class DefaultJsonDeserializationContext implements JsonDeserializationContext {
        @Override
        public <T> T deserialize(JsonElement json, Type typeOfT) throws JsonParseException {
            Gson gson = FreeturiloGson.getFreeturiloDeserializingGson();
            return gson.fromJson(json, typeOfT);
        }
    }

    @Test
    public void deserialize_station() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("name", new JsonPrimitive("Metro Trocka"));
        jsonObject.add("latitude", new JsonPrimitive(52.275799));
        jsonObject.add("longitude", new JsonPrimitive(21.056083));
        jsonObject.add("type", new JsonPrimitive("Station"));
        jsonObject.add("id", new JsonPrimitive(2585942));
        jsonObject.add("bikeRacks", new JsonPrimitive(8));
        jsonObject.add("bikes", new JsonPrimitive(14));
        jsonObject.add("state", new JsonPrimitive(0));
        LocationDeserializer deserializer = new LocationDeserializer();
        JsonDeserializationContext context = new DefaultJsonDeserializationContext();

        Station station = (Station) deserializer.deserialize(jsonObject, null, context);

        assertEquals("Metro Trocka", station.name);
        assertEquals(52.275799, station.latitude, 0.000001);
        assertEquals(21.056083, station.longitude, 0.000001);
        assertEquals(2585942, station.id);
        assertEquals(8, station.bikeRacks);
        assertEquals(14, station.bikes);
        assertEquals(0, station.state);
    }

    @Test
    public void deserialize_favourite() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("name", new JsonPrimitive("MiNI"));
        jsonObject.add("latitude", new JsonPrimitive(52.221990));
        jsonObject.add("longitude", new JsonPrimitive(21.0070651));
        jsonObject.add("type", new JsonPrimitive("Favourite.SCHOOL"));
        LocationDeserializer deserializer = new LocationDeserializer();
        JsonDeserializationContext context = new DefaultJsonDeserializationContext();

        Favourite favourite = (Favourite) deserializer.deserialize(jsonObject, null, context);

        assertEquals("MiNI", favourite.name);
        assertEquals(52.221990, favourite.latitude, 0.000001);
        assertEquals(21.0070651, favourite.longitude, 0.000001);
        assertEquals(FavouriteType.SCHOOL, favourite.type);
    }

    @Test
    public void deserialize_location() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("name", new JsonPrimitive("Westfield Arkadia"));
        jsonObject.add("latitude", new JsonPrimitive(52.256128));
        jsonObject.add("longitude", new JsonPrimitive(20.985926));
        jsonObject.add("type", new JsonPrimitive("Location"));
        LocationDeserializer deserializer = new LocationDeserializer();

        Location location = deserializer.deserialize(jsonObject, null, null);

        assertEquals("Westfield Arkadia", location.name);
        assertEquals(52.256128, location.latitude, 0.000001);
        assertEquals(20.985926, location.longitude, 0.000001);
    }
}