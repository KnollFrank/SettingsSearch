package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.OutputStream;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreenData;

public class MergedPreferenceScreenDataDAO {

    public static void persist(final MergedPreferenceScreenData mergedPreferenceScreenData,
                               final OutputStream sink1,
                               final OutputStream sink2,
                               final OutputStream sink3) {
        JsonDAO.persist(mergedPreferenceScreenData.allPreferencesForSearch(), sink1);
        JsonDAO.persist(mergedPreferenceScreenData.preferencePathByPreferenceId(), sink2);
        JsonDAO.persist(mergedPreferenceScreenData.hostByPreferenceId(), sink3);
    }

    public static MergedPreferenceScreenData load(final InputStream source1,
                                                  final InputStream source2,
                                                  final InputStream source3) {
        return new MergedPreferenceScreenData(
                JsonDAO.load(
                        source1,
                        new TypeToken<>() {
                        }),
                JsonDAO.load(
                        source2,
                        new TypeToken<>() {
                        }),
                JsonDAO.load(
                        source3,
                        new TypeToken<>() {
                        }));
    }
}
