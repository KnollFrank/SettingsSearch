package de.KnollFrank.lib.settingssearch.common;

import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public class SearchablePreferences {

    public static Set<SearchablePreference> getPreferencesRecursively(final SearchablePreference preference) {
        return ImmutableSet
                .<SearchablePreference>builder()
                .add(preference)
                .addAll(getPreferencesRecursively(preference.getChildren()))
                .build();
    }

    public static Set<SearchablePreference> getPreferencesRecursively(final Collection<SearchablePreference> preferences) {
        return Sets.union(
                preferences
                        .stream()
                        .map(SearchablePreferences::getPreferencesRecursively)
                        .collect(Collectors.toSet()));
    }
}
