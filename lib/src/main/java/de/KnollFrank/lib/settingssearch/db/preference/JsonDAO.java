package de.KnollFrank.lib.settingssearch.db.preference;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.Type;

class JsonDAO {

    public static <T> void persist(final T source, final OutputStream sink) {
        persist(toJson(source), sink);
    }

    public static <T> T load(final InputStream source, final Type type) {
        return fromJson(new InputStreamReader(source), type);
    }

    private static <T> String toJson(final T src) {
        return getGson().toJson(src);
    }

    private static <T> T fromJson(final Reader json, final Type type) {
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
