package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Optional;

class OptionalTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
        return isOptionalType(typeToken) ?
                (TypeAdapter<T>) new OptionalTypeAdapter<>(getElementTypeAdapter(gson, typeToken)).nullSafe() :
                null;
    }

    private static <T> boolean isOptionalType(final TypeToken<T> typeToken) {
        return typeToken.getRawType() == Optional.class; // FK-FIXME: does not work: && typeToken.getType() instanceof ParameterizedType;
    }

    private static <T> TypeAdapter<?> getElementTypeAdapter(final Gson gson, final TypeToken<T> typeToken) {
        final Type elementType = Integer.class; // FK-FIXME: does not work: ((ParameterizedType) typeToken.getType()).getActualTypeArguments()[0];
        return gson.getAdapter(TypeToken.get(elementType));
    }
}