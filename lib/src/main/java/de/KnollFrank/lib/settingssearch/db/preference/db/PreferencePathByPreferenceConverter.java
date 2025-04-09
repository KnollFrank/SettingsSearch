package de.KnollFrank.lib.settingssearch.db.preference.db;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

class PreferencePathByPreferenceConverter {

    public static Map<Integer, List<Integer>> addIds(final Set<SearchablePreference> preferences) {
        return preferences
                .stream()
                .collect(
                        Collectors.toMap(
                                SearchablePreference::getId,
                                preference -> PreferencePathConverter.addIds(preference.getPreferencePath())));
    }

    public static Map<SearchablePreference, PreferencePath> removeIds(
            final Map<Integer, List<Integer>> preferencePathIdsByPreferenceId,
            final Map<Integer, SearchablePreference> preferenceById) {
        return preferencePathIdsByPreferenceId
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                entry -> preferenceById.get(entry.getKey()),
                                entry -> PreferencePathConverter.removeIds(entry.getValue(), preferenceById)));
    }
}
