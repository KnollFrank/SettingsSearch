package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.OutputStream;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;

public class MergedPreferenceScreenDataDAO {

    public static void persist(final MergedPreferenceScreenData mergedPreferenceScreenData,
                               final OutputStream allPreferencesForSearch,
                               final OutputStream preferencePathByPreference,
                               final OutputStream hostByPreference) {
        JsonDAO.persist(mergedPreferenceScreenData.preferences(), allPreferencesForSearch);
        // FK-TODO: just persist the id's of the preferences and PreferencePaths
        JsonDAO.persist(mergedPreferenceScreenData.preferencePathByPreference(), preferencePathByPreference);
        // FK-TODO: just persist the id's of the preferences
        JsonDAO.persist(mergedPreferenceScreenData.hostByPreference(), hostByPreference);
    }

    public static MergedPreferenceScreenData load(final InputStream allPreferencesForSearch,
                                                  final InputStream preferencePathByPreference,
                                                  final InputStream hostByPreference) {
        return new MergedPreferenceScreenData(
                JsonDAO.load(
                        allPreferencesForSearch,
                        new TypeToken<>() {
                        }),
                JsonDAO.load(
                        preferencePathByPreference,
                        new TypeToken<>() {
                        }),
                JsonDAO.load(
                        hostByPreference,
                        new TypeToken<>() {
                        }));
    }
}
