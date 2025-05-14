package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.IOUtils;

public class JsonDAO {

    private static Optional<Gson> INSTANCE = Optional.empty();

    public static <T> void persist(final T source, final OutputStream sink) {
        IOUtils.persist(toJson(source), sink);
    }

    public static <T> T load(final InputStream source, final TypeToken<T> type) {
        return fromJson(new InputStreamReader(source), type);
    }

    private static <T> String toJson(final T src) {
        return getGson().toJson(src);
    }

    private static <T> T fromJson(final Reader json, final TypeToken<T> type) {
        return getGson().fromJson(json, type);
    }

    private static Gson getGson() {
        if (INSTANCE.isEmpty()) {
            INSTANCE = Optional.of(createGson());
        }
        return INSTANCE.orElseThrow();
    }

    private static Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Class.class, new ClassTypeAdapter())
                .registerTypeAdapterFactory(new EitherIntegerOrStringTypeAdapterFactory())
                .registerTypeAdapterFactory(new OptionalTypeAdapterFactory())
                .registerTypeAdapterFactory(new BundleTypeAdapterFactory())
                .setExclusionStrategies(new AnnotationExclusionStrategy())
                .enableComplexMapKeySerialization()
                .create();
    }
}
