package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.io.InputStream;
import java.io.OutputStream;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenDataWithIds;

public class MergedPreferenceScreenDataDAO {

    public static void persist(final MergedPreferenceScreenData mergedPreferenceScreenData,
                               final OutputStream preferences,
                               final OutputStream preferencePathByPreference,
                               final OutputStream hostByPreference) {
        final MergedPreferenceScreenDataWithIds mergedPreferenceScreenDataWithIds =
                MergedPreferenceScreenDataConverter.addIds(mergedPreferenceScreenData);
        MergedPreferenceScreenDataWithIdsDAO.persist(
                mergedPreferenceScreenDataWithIds,
                preferences,
                preferencePathByPreference,
                hostByPreference);
    }

    public static MergedPreferenceScreenData load(final InputStream preferences,
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