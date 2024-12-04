package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenDataWithIds;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class MergedPreferenceScreenDataDAO {

    public static void persist(final Set<SearchablePreferencePOJO> searchablePreferencePOJOs,
                               final OutputStream preferences,
                               final OutputStream preferencePathByPreference,
                               final OutputStream hostByPreference) {
        final MergedPreferenceScreenDataWithIds mergedPreferenceScreenDataWithIds =
                MergedPreferenceScreenDataConverter.addIds(searchablePreferencePOJOs);
        MergedPreferenceScreenDataWithIdsDAO.persist(
                mergedPreferenceScreenDataWithIds,
                preferences,
                preferencePathByPreference,
                hostByPreference);
    }

    public static Set<SearchablePreferencePOJO> load(final InputStream preferences,
                                                     final InputStream preferencePathByPreference,
                                                     final InputStream hostByPreference) {
        final MergedPreferenceScreenDataWithIds mergedPreferenceScreenDataWithIds =
                MergedPreferenceScreenDataWithIdsDAO.load(
                        preferences,
                        preferencePathByPreference,
                        hostByPreference);
        return MergedPreferenceScreenDataConverter.removeIds(mergedPreferenceScreenDataWithIds);
    }
}
