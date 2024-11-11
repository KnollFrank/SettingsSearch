package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.codepoetics.ambivalence.Either;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

class EitherIntegerOrStringTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
        return isEitherIntegerOrStringType(typeToken) ?
                (TypeAdapter<T>) new EitherIntegerOrStringTypeAdapter().nullSafe() :
                null;
    }

    private static <T> boolean isEitherIntegerOrStringType(final TypeToken<T> typeToken) {
        return typeToken.getRawType() == Either.class
                && typeToken.getType() instanceof final ParameterizedType parameterizedType
                && getLeftType(parameterizedType) == Integer.class
                && getRightType(parameterizedType) == String.class;
    }

    private static Type getLeftType(final ParameterizedType parameterizedType) {
        return parameterizedType.getActualTypeArguments()[0];
    }

    private static Type getRightType(final ParameterizedType parameterizedType) {
        return parameterizedType.getActualTypeArguments()[1];
    }
}