package de.KnollFrank.lib.settingssearch.common;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.MoreCollectors;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
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

    public static SearchablePreference findUniquePreferenceRecursivelyByPredicate(
            final Set<SearchablePreference> preferences,
            final Predicate<SearchablePreference> predicate) {
        return SearchablePreferences
                .getPreferencesRecursively(preferences)
                .stream()
                .filter(predicate)
                .collect(MoreCollectors.onlyElement());
    }
}
