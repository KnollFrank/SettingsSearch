package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PreferenceAndPredecessors {

    public static Map<SearchablePreference, SearchablePreference> getPredecessorByPreference(final Set<PreferenceAndPredecessor> preferencesAndPredecessors) {
        return preferencesAndPredecessors
                .stream()
                .filter(preferenceAndPredecessor -> preferenceAndPredecessor.getPredecessor().isPresent())
                .collect(
                        Collectors.toMap(
                                PreferenceAndPredecessor::getPreference,
                                preferenceAndPredecessor -> preferenceAndPredecessor.getPredecessor().orElseThrow()));
    }
}
