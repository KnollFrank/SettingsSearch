package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.Sets;

public class SearchablePreferenceEntities {

    public static Optional<SearchablePreferenceEntity> findPreferenceByKey(final Set<SearchablePreferenceEntity> preferences, final String key) {
        return Sets.findElementByPredicate(
                preferences,
                searchablePreference -> searchablePreference.key().equals(key));
    }

    public static Optional<SearchablePreferenceEntity> findPreferenceById(final Set<SearchablePreferenceEntity> preferences,
                                                                          final String id) {
        return Sets.findElementByPredicate(
                preferences,
                searchablePreference -> searchablePreference.id().equals(id));
    }
}
