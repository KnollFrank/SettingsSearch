package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.codepoetics.ambivalence.Either;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

class EitherTypeAdapterFactory implements TypeAdapterFactory {

    // FK-TODO: refactor
    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
        final Type type = typeToken.getType();
        if (typeToken.getRawType() != Either.class || !(type instanceof ParameterizedType)) {
            return null;
        }

        final Type leftType = ((ParameterizedType) type).getActualTypeArguments()[0];
        final Type rightType = ((ParameterizedType) type).getActualTypeArguments()[1];
        if (leftType != Integer.class || rightType != String.class) {
            return null;
        }
        return (TypeAdapter<T>) new EitherTypeAdapter().nullSafe();
    }
}
