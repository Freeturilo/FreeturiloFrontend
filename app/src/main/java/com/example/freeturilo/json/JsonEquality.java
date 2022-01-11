package com.example.freeturilo.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Collection of static methods that help with json data comparison.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see JsonElement
 */
public class JsonEquality {

    /**
     * Checks whether the actual json element equals the expected one.
     * <p>
     * Equality between json elements is defined as follows. If both elements
     * are null, they are equal. If both elements are json primitives, they are
     * only equal if they have the same value. If both elements are json
     * objects, they are only equal if all fields of the actual json object
     * exist in the expected one and if they have the same value in both
     * objects. If both elements are json arrays, they are only equal if both
     * have the same size and their corresponding elements are equal.
     * @param expected      an expected json element
     * @param actual        an actual json element
     * @return              a boolean indicating whether the json elements are
     *                      equal
     */
    public static boolean equals(JsonElement expected, JsonElement actual) {
        if (expected.isJsonNull() && actual.isJsonNull())
            return true;
        if (expected.isJsonPrimitive() && actual.isJsonPrimitive())
            return expected.equals(actual);
        if (expected.isJsonObject() && actual.isJsonObject())
            return equals(expected.getAsJsonObject(), actual.getAsJsonObject());
        if (expected.isJsonArray() && actual.isJsonArray())
            return equals(expected.getAsJsonArray(), actual.getAsJsonArray());
        return false;
    }

    /**
     * Checks whether the actual json object equals the expected one.
     * <p>
     * Json objects are only equal if all fields of the actual json object
     * exist in the expected one and if they have the same value in both
     * objects.
     * @param expected      an expected json object
     * @param actual        an actual json object
     * @return              a boolean indicating whether the json objects are
     *                      equal
     */
    private static boolean equals(JsonObject expected, JsonObject actual) {
        for (String key : expected.keySet())
            if (!actual.has(key) || !equals(expected.get(key), actual.get(key)))
                return false;
        return true;
    }

    /**
     * Checks whether the actual json array equals the expected one.
     * <p>
     * Json arrays are only equal if both have the same size and their
     * corresponding elements are equal.
     * @param expected      an expected json array
     * @param actual        an actual json array
     * @return              a boolean indicating whether the json arrays are
     *                      equal
     */
    private static boolean equals(JsonArray expected, JsonArray actual) {
        if (expected.size() != actual.size())
            return false;
        for (int i = 0; i < expected.size(); i++)
            if (!equals(expected.get(i), expected.get(i)))
                return false;
        return true;
    }
}
