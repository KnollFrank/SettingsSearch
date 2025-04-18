package de.KnollFrank.lib.settingssearch.db.preference.db.file;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public class MergedPreferenceScreenDataDAO {

    public static void persist(final Set<SearchablePreference> searchablePreferences,
                               final OutputStream preferences,
                               final OutputStream predecessorIdByPreferenceId) {
        final MergedPreferenceScreenDataWithIds mergedPreferenceScreenDataWithIds =
                MergedPreferenceScreenDataConverter.addIds(searchablePreferences);
        MergedPreferenceScreenDataWithIdsDAO.persist(
                mergedPreferenceScreenDataWithIds,
                preferences,
                predecessorIdByPreferenceId);
    }

    public static Set<SearchablePreference> load(final InputStream preferences,
                                                 final InputStream preferencePathByPreference) {
        final MergedPreferenceScreenDataWithIds mergedPreferenceScreenDataWithIds =
                MergedPreferenceScreenDataWithIdsDAO.load(
                        preferences,
                        preferencePathByPreference);
        return MergedPreferenceScreenDataConverter.removeIds(mergedPreferenceScreenDataWithIds);
    }
}
