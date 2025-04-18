package de.KnollFrank.lib.settingssearch.db.preference.db.file;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PredecessorSetter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

class MergedPreferenceScreenDataConverter {

    public static MergedPreferenceScreenDataWithIds addIds(final Set<SearchablePreference> preferences) {
        return new MergedPreferenceScreenDataWithIds(
                preferences,
                PredecessorByPreferenceConverter.addIds(SearchablePreferences.getPreferencesRecursively(preferences)));
    }

    public static Set<SearchablePreference> removeIds(final MergedPreferenceScreenDataWithIds mergedPreferenceScreenDataWithIds) {
        final PredecessorSetter predecessorSetter =
                new PredecessorSetter(
                        PredecessorByPreferenceConverter.removeIds(
                                mergedPreferenceScreenDataWithIds.predecessorIdByPreferenceId(),
                                getPreferenceById(mergedPreferenceScreenDataWithIds.preferences())));
        predecessorSetter.setPredecessors(mergedPreferenceScreenDataWithIds.preferences());
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
