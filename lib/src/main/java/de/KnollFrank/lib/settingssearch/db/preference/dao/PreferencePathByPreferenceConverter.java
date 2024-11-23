package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

class PreferencePathByPreferenceConverter {

    public static Map<Integer, List<Integer>> addIds(
            final Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference) {
        return preferencePathByPreference
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey().getId(),
                                entry -> PreferencePathConverter.addIds(entry.getValue())));
    }

    public static Map<SearchablePreferencePOJO, PreferencePath> removeIds(
            final Map<Integer, List<Integer>> preferencePathIdByPreferenceId,
            final Map<Integer, SearchablePreferencePOJO> preferenceById) {
        return preferencePathIdByPreferenceId
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                entry -> preferenceById.get(entry.getKey()),
                                entry -> PreferencePathConverter.removeIds(entry.getValue(), preferenceById)));
    }
}
