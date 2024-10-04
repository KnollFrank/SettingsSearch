package de.KnollFrank.lib.settingssearch.db.preference;

import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.OutputStream;

public class SearchablePreferenceScreenPOJODAO {

    public static void persist(final SearchablePreferenceScreenPOJO source, final OutputStream sink) {
        JsonDAO.persist(source, sink);
    }

    public static SearchablePreferenceScreenPOJO load(final InputStream source) {
        return JsonDAO.load(
                source,
                new TypeToken<>() {
                });
    }
}
