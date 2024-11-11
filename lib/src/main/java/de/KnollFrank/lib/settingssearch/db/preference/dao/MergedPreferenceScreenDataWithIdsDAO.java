package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenDataWithIds;

class MergedPreferenceScreenDataWithIdsDAO {

    public static void persist(final MergedPreferenceScreenDataWithIds mergedPreferenceScreenDataWithIds,
                               final SearchablePreferencePOJODAO searchablePreferencePOJODAO,
                               final OutputStream preferences,
                               final OutputStream preferencePathIdByPreferenceId,
                               final OutputStream hostByPreferenceId) {
        searchablePreferencePOJODAO.deleteAll();
        searchablePreferencePOJODAO.insertAll(mergedPreferenceScreenDataWithIds.preferences());
        JsonDAO.persist(mergedPreferenceScreenDataWithIds.preferences(), preferences);
        JsonDAO.persist(mergedPreferenceScreenDataWithIds.preferencePathIdByPreferenceId(), preferencePathIdByPreferenceId);
        JsonDAO.persist(mergedPreferenceScreenDataWithIds.hostByPreferenceId(), hostByPreferenceId);
    }

    public static MergedPreferenceScreenDataWithIds load(final SearchablePreferencePOJODAO searchablePreferencePOJODAO,
                                                         final InputStream preferences,
                                                         final InputStream preferencePathIdByPreferenceId,
                                                         final InputStream hostByPreferenceId) {

        return new MergedPreferenceScreenDataWithIds(
                new HashSet<>(searchablePreferencePOJODAO.getAll()),
                JsonDAO.load(
                        preferencePathIdByPreferenceId,
                        new TypeToken<>() {
                        }),
                JsonDAO.load(
                        hostByPreferenceId,
                        new TypeToken<>() {
                        }));
    }
}
