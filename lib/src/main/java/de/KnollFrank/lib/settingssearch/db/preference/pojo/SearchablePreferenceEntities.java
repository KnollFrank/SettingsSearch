package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import com.google.common.collect.MoreCollectors;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

// FK-TODO: DRY with SearchablePreferences
public class SearchablePreferenceEntities {

    public static Optional<SearchablePreferenceEntity> findPreferenceByPredicate(
            final Set<SearchablePreferenceEntity> preferences,
            final Predicate<SearchablePreferenceEntity> predicate) {
        return preferences
                .stream()
                .filter(predicate)
                .collect(MoreCollectors.toOptional());
    }

    public static Optional<SearchablePreferenceEntity> findPreferenceByKey(final Set<SearchablePreferenceEntity> preferences, final String key) {
        return findPreferenceByPredicate(
                preferences,
                searchablePreference -> searchablePreference.getKey().equals(key));
    }

    public static Optional<SearchablePreferenceEntity> findPreferenceById(final Set<SearchablePreferenceEntity> preferences, final int id) {
        return findPreferenceByPredicate(
                preferences,
                searchablePreference -> searchablePreference.getId() == id);
    }
}
