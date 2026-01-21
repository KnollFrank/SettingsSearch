package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class PreferenceAndPredecessors {

    private PreferenceAndPredecessors() {
    }

    public static Map<SearchablePreferenceEntity, Optional<SearchablePreferenceEntity>> getPredecessorByPreference(final Set<PreferenceAndPredecessor> preferencesAndPredecessors) {
        return preferencesAndPredecessors
                .stream()
                .collect(
                        Collectors.toMap(
                                PreferenceAndPredecessor::getPreference,
                                PreferenceAndPredecessor::getPredecessor));
    }
}
