package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenDataWithIds;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

class MergedPreferenceScreenDataWithIdsDAO {

    public static void persist(final MergedPreferenceScreenDataWithIds mergedPreferenceScreenDataWithIds,
                               final OutputStream preferences,
                               final OutputStream preferencePathIdByPreferenceId,
                               final OutputStream hostByPreferenceId) {
        persistPreferences(mergedPreferenceScreenDataWithIds.preferences(), preferences);
        JsonDAO.persist(mergedPreferenceScreenDataWithIds.preferencePathIdByPreferenceId(), preferencePathIdByPreferenceId);
        JsonDAO.persist(mergedPreferenceScreenDataWithIds.hostByPreferenceId(), hostByPreferenceId);
    }

    public static MergedPreferenceScreenDataWithIds load(final InputStream preferences,
                                                         final InputStream preferencePathIdByPreferenceId,
                                                         final InputStream hostByPreferenceId) {
        return new MergedPreferenceScreenDataWithIds(
                loadPreferences(preferences),
                JsonDAO.load(
                        preferencePathIdByPreferenceId,
                        new TypeToken<>() {
                        }),
                JsonDAO.load(
                        hostByPreferenceId,
                        new TypeToken<>() {
                        }));
    }

    private static void persistPreferences(final Set<SearchablePreferencePOJO> preferences, final OutputStream outputStream) {
        try {
            new ObjectOutputStream(outputStream).writeObject(preferences);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Set<SearchablePreferencePOJO> loadPreferences(final InputStream inputStream) {
        try {
            return (Set<SearchablePreferencePOJO>) new ObjectInputStream(inputStream).readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}