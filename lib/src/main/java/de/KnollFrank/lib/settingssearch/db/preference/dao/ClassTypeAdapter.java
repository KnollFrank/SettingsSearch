package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

// adapted from https://stackoverflow.com/a/44184399
public class ClassTypeAdapter implements JsonSerializer<Class<?>>, JsonDeserializer<Class<?>> {

    @Override
    public JsonElement serialize(final Class<?> src, final Type typeOfSrc, final JsonSerializationContext context) {
        return new JsonPrimitive(src.getName());
    }

    @Override
    public Class<?> deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        try {
            return Class.forName(json.getAsString());
        } catch (final ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }
}