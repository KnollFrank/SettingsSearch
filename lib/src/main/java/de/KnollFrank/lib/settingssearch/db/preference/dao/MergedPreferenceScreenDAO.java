package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.OutputStream;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;

public class MergedPreferenceScreenDAO {

    public static void persist(final MergedPreferenceScreen mergedPreferenceScreen, final OutputStream sink) {
        JsonDAO.persist(mergedPreferenceScreen.allPreferencesForSearch(), sink);
    }

    public static MergedPreferenceScreen load(final InputStream source) {
        return new MergedPreferenceScreen(
                JsonDAO.load(
                        source,
                        new TypeToken<>() {
                        }),
                null,
                null);
    }
}
