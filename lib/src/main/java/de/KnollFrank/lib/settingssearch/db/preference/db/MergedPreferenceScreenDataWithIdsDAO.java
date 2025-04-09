package de.KnollFrank.lib.settingssearch.db.preference.db;

import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.OutputStream;

import de.KnollFrank.lib.settingssearch.db.preference.dao.JsonDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenDataWithIds;

class MergedPreferenceScreenDataWithIdsDAO {

    public static void persist(final MergedPreferenceScreenDataWithIds mergedPreferenceScreenDataWithIds,
                               final OutputStream preferences,
                               final OutputStream preferencePathIdsByPreferenceId,
                               final OutputStream hostByPreferenceId) {
        JsonDAO.persist(mergedPreferenceScreenDataWithIds.preferences(), preferences);
        JsonDAO.persist(mergedPreferenceScreenDataWithIds.preferencePathIdsByPreferenceId(), preferencePathIdsByPreferenceId);
        JsonDAO.persist(mergedPreferenceScreenDataWithIds.hostByPreferenceId(), hostByPreferenceId);
    }

    public static MergedPreferenceScreenDataWithIds load(final InputStream preferences,
                                                         final InputStream preferencePathIdsByPreferenceId,
                                                         final InputStream hostByPreferenceId) {
        return new MergedPreferenceScreenDataWithIds(
                JsonDAO.load(
                        preferences,
                        new TypeToken<>() {
                        }),
                JsonDAO.load(
                        preferencePathIdsByPreferenceId,
                        new TypeToken<>() {
                        }),
                JsonDAO.load(
                        hostByPreferenceId,
                        new TypeToken<>() {
                        }));
    }
}
