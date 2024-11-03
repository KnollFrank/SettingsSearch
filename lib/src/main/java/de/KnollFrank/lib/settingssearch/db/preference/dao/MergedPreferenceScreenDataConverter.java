package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenDataWithIds;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

class MergedPreferenceScreenDataConverter {

    public static MergedPreferenceScreenDataWithIds addIds(
            final MergedPreferenceScreenData mergedPreferenceScreenData) {
        return new MergedPreferenceScreenDataWithIds(
                mergedPreferenceScreenData.preferences(),
                PreferencePathByPreferenceConverter.addIds(mergedPreferenceScreenData.preferencePathByPreference()),
                HostByPreferenceConverter.addIds(mergedPreferenceScreenData.hostByPreference()));
    }

    public static MergedPreferenceScreenData removeIds(final MergedPreferenceScreenDataWithIds mergedPreferenceScreenDataWithIds) {
        final Map<Integer, SearchablePreferencePOJO> preferenceById = getPreferenceById(mergedPreferenceScreenDataWithIds.preferences());
        return new MergedPreferenceScreenData(
                mergedPreferenceScreenDataWithIds.preferences(),
                PreferencePathByPreferenceConverter.removeIds(
                        mergedPreferenceScreenDataWithIds.preferencePathIdByPreferenceId(),
                        preferenceById),
                HostByPreferenceConverter.removeIds(
                        mergedPreferenceScreenDataWithIds.hostByPreferenceId(),
                        preferenceById));
    }

    private static Map<Integer, SearchablePreferencePOJO> getPreferenceById(final Set<SearchablePreferencePOJO> preferences) {
        return preferences
                .stream()
                .collect(
                        Collectors.toMap(
                                SearchablePreferencePOJO::id,
                                Function.identity()));
    }
}
