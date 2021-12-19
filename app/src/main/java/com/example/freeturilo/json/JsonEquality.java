package com.example.freeturilo.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonEquality {

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

    private static boolean equals(JsonObject expected, JsonObject actual) {
        for (String key : expected.keySet())
            if (!actual.has(key) || !equals(expected.get(key), actual.get(key)))
                return false;
        return true;
    }

    private static boolean equals(JsonArray expected, JsonArray actual) {
        if (expected.size() != actual.size())
            return false;
        for (int i = 0; i < expected.size(); i++)
            if (!equals(expected.get(i), expected.get(i)))
                return false;
        return true;
    }
}
