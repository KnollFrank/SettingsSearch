package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import com.google.common.collect.MoreCollectors;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class SearchablePreferences {

    public static Optional<SearchablePreference> findPreferenceByPredicate(
            final Set<SearchablePreference> preferences,
            final Predicate<SearchablePreference> predicate) {
        return preferences
                .stream()
                .filter(predicate)
                .collect(MoreCollectors.toOptional());
    }

    public static Optional<SearchablePreference> findPreferenceByKey(final Set<SearchablePreference> preferences, final String key) {
        return findPreferenceByPredicate(
                preferences,
                searchablePreference -> searchablePreference.getKey().equals(key));
    }

    public static Optional<SearchablePreference> findPreferenceById(final Set<SearchablePreference> preferences, final int id) {
        return findPreferenceByPredicate(
                        preferences,
                        searchablePreference -> searchablePreference.getId() == id);
    }
}
