package com.example.freeturilo.json;

import com.example.freeturilo.core.Criterion;
import com.example.freeturilo.core.Favourite;
import com.example.freeturilo.core.IdentifiedLocation;
import com.example.freeturilo.core.Location;
import com.example.freeturilo.core.Station;
import com.example.freeturilo.core.SystemState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.model.Distance;
import com.google.maps.model.Duration;

/**
 * Collection of static methods that help with serialization to JSON and
 * deserialization from JSON within data transaction with Freeturilo API.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #getFreeturiloSerializingGson
 * @see #getFreeturiloDeserializingGson
 * @see Gson
 * @see GsonBuilder
 */
public class FreeturiloGson {

    /**
     * Gets a Gson instance used for serialization of objects sent to
     * Freeturilo API within data transaction.
     * @return      a gson instance with all required serializers registered
     */
    public static Gson getFreeturiloSerializingGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Criterion.class, new CriterionSerializer());
        gsonBuilder.registerTypeAdapter(Favourite.class, new FavouriteSerializer());
        gsonBuilder.registerTypeAdapter(IdentifiedLocation.class, new LocationSerializer<>());
        gsonBuilder.registerTypeAdapter(Location.class, new LocationSerializer<>());
        gsonBuilder.registerTypeAdapter(Station.class, new StationSerializer());
        return gsonBuilder.create();
    }

    /**
     * Gets a Gson instance used for deserialization of objects received from
     * Freeturilo API within data transaction.
     * @return      a gson instance with all required deserializers registered
     */
    public static Gson getFreeturiloDeserializingGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Criterion.class, new CriterionDeserializer());
        gsonBuilder.registerTypeAdapter(Distance.class, new DistanceDeserializer());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationDeserializer());
        gsonBuilder.registerTypeAdapter(Favourite.class, new FavouriteDeserializer());
        gsonBuilder.registerTypeAdapter(Location.class, new LocationDeserializer());
        gsonBuilder.registerTypeAdapter(SystemState.class, new SystemStateDeserializer());
        return gsonBuilder.create();
    }
}
