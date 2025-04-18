package de.KnollFrank.lib.settingssearch.db.preference.db.file;

import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.OutputStream;

import de.KnollFrank.lib.settingssearch.db.preference.dao.JsonDAO;

class MergedPreferenceScreenDataWithIdsDAO {

    public static void persist(final MergedPreferenceScreenDataWithIds mergedPreferenceScreenDataWithIds,
                               final OutputStream preferences,
                               final OutputStream predecessorIdByPreferenceId) {
        JsonDAO.persist(mergedPreferenceScreenDataWithIds.preferences(), preferences);
        JsonDAO.persist(mergedPreferenceScreenDataWithIds.predecessorIdByPreferenceId(), predecessorIdByPreferenceId);
    }

    public static MergedPreferenceScreenDataWithIds load(final InputStream preferences,
                                                         final InputStream predecessorIdByPreferenceId) {
        return new MergedPreferenceScreenDataWithIds(
                JsonDAO.load(
                        preferences,
                        new TypeToken<>() {
                        }),
                JsonDAO.load(
                        predecessorIdByPreferenceId,
                        new TypeToken<>() {
                        }));
    }
}
