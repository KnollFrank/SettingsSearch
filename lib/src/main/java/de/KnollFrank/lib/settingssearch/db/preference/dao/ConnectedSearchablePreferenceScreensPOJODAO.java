package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.OutputStream;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.ConnectedSearchablePreferenceScreensPOJO;

public class ConnectedSearchablePreferenceScreensPOJODAO {

    public static void persist(final ConnectedSearchablePreferenceScreensPOJO source, final OutputStream sink) {
        JsonDAO.persist(source, sink);
    }

    public static ConnectedSearchablePreferenceScreensPOJO load(final InputStream source) {
        return JsonDAO.load(
                source,
                new TypeToken<>() {
                });
    }
}
