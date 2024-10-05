package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.OutputStream;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenPOJO;

// FK-TODO: remove class?
class SearchablePreferenceScreenPOJODAO {

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
