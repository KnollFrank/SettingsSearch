package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.PreferencePOJOs;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenDataWithIds;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferencePathsAndHostsSetter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

class MergedPreferenceScreenDataConverter {

    public static MergedPreferenceScreenDataWithIds addIds(final Set<SearchablePreferencePOJO> preferences) {
        final Set<SearchablePreferencePOJO> preferencesRecursively = PreferencePOJOs.getPreferencesRecursively(preferences);
        return new MergedPreferenceScreenDataWithIds(
                preferences,
                PreferencePathByPreferenceConverter.addIds(preferencesRecursively),
                HostByPreferenceConverter.addIds(preferencesRecursively));
    }

    public static Set<SearchablePreferencePOJO> removeIds(final MergedPreferenceScreenDataWithIds mergedPreferenceScreenDataWithIds) {
        final Map<Integer, SearchablePreferencePOJO> preferenceById = getPreferenceById(mergedPreferenceScreenDataWithIds.preferences());
        final PreferencePathsAndHostsSetter preferencePathsAndHostsSetter =
                new PreferencePathsAndHostsSetter(
                        PreferencePathByPreferenceConverter.removeIds(
                                mergedPreferenceScreenDataWithIds.preferencePathIdsByPreferenceId(),
                                preferenceById),
                        HostByPreferenceConverter.removeIds(
                                mergedPreferenceScreenDataWithIds.hostByPreferenceId(),
                                preferenceById));
        preferencePathsAndHostsSetter.setPreferencePathsAndHosts(mergedPreferenceScreenDataWithIds.preferences());
        return mergedPreferenceScreenDataWithIds.preferences();
    }

    private static Map<Integer, SearchablePreferencePOJO> getPreferenceById(final Set<SearchablePreferencePOJO> preferences) {
        return PreferencePOJOs
                .getPreferencesRecursively(preferences)
                .stream()
                .collect(
                        Collectors.toMap(
                                SearchablePreferencePOJO::getId,
                                Function.identity()));
    }
}
