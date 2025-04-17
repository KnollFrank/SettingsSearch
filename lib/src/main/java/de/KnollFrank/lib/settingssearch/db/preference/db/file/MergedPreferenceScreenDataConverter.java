package de.KnollFrank.lib.settingssearch.db.preference.db.file;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferencePathsSetter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

class MergedPreferenceScreenDataConverter {

    public static MergedPreferenceScreenDataWithIds addIds(final Set<SearchablePreference> preferences) {
        final Set<SearchablePreference> preferencesRecursively = SearchablePreferences.getPreferencesRecursively(preferences);
        return new MergedPreferenceScreenDataWithIds(
                preferences,
                PreferencePathByPreferenceConverter.addIds(preferencesRecursively),
                HostByPreferenceConverter.addIds(preferencesRecursively));
    }

    public static Set<SearchablePreference> removeIds(final MergedPreferenceScreenDataWithIds mergedPreferenceScreenDataWithIds) {
        final Map<Integer, SearchablePreference> preferenceById = getPreferenceById(mergedPreferenceScreenDataWithIds.preferences());
        final PreferencePathsSetter preferencePathsSetter =
                new PreferencePathsSetter(
                        PreferencePathByPreferenceConverter.removeIds(
                                mergedPreferenceScreenDataWithIds.preferencePathIdsByPreferenceId(),
                                preferenceById));
        preferencePathsSetter.setPreferencePaths(mergedPreferenceScreenDataWithIds.preferences());
        return mergedPreferenceScreenDataWithIds.preferences();
    }

    private static Map<Integer, SearchablePreference> getPreferenceById(final Set<SearchablePreference> preferences) {
        return SearchablePreferences
                .getPreferencesRecursively(preferences)
                .stream()
                .collect(
                        Collectors.toMap(
                                SearchablePreference::getId,
                                Function.identity()));
    }
}
