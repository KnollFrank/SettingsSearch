package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;

public class JsonDAO {

    public static <T> void persist(final T source, final OutputStream sink) {
        persist(toJson(source), sink);
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
        return new Gson();
    }

    private static void persist(final String source, final OutputStream sink) {
        try (final PrintWriter writer = new PrintWriter(sink)) {
            writer.print(source);
            writer.flush();
        }
    }
}
