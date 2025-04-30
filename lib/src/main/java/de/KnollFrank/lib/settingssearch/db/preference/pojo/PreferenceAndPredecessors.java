package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PreferenceAndPredecessors {

    public static Map<SearchablePreference, SearchablePreference> getPredecessorByPreference(final List<PreferenceAndPredecessor> preferencesAndPredecessors) {
        return preferencesAndPredecessors
                .stream()
                .filter(preferenceAndPredecessor -> preferenceAndPredecessor.getPredecessor().isPresent())
                .collect(
                        Collectors.toMap(
                                PreferenceAndPredecessor::getPreference,
                                preferenceAndPredecessor -> preferenceAndPredecessor.getPredecessor().orElseThrow()));
    }
}
