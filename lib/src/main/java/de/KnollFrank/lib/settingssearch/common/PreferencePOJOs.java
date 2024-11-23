package de.KnollFrank.lib.settingssearch.common;

import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class PreferencePOJOs {

    public static Set<SearchablePreferencePOJO> getPreferencesRecursively(final SearchablePreferencePOJO preference) {
        return ImmutableSet
                .<SearchablePreferencePOJO>builder()
                .add(preference)
                .addAll(getPreferencesRecursively(preference.getChildren()))
                .build();
    }

    public static Set<SearchablePreferencePOJO> getPreferencesRecursively(final Collection<SearchablePreferencePOJO> preferences) {
        return Sets.union(
                preferences
                        .stream()
                        .map(PreferencePOJOs::getPreferencesRecursively)
                        .collect(Collectors.toSet()));
    }
}
