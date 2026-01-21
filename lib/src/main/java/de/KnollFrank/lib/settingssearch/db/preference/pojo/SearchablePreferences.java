package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.Sets;

public class SearchablePreferences {

    private SearchablePreferences() {
    }

    public static Optional<SearchablePreference> findPreferenceByKey(final Set<SearchablePreference> preferences,
                                                                     final String key) {
        return Sets.findElementByPredicate(
                preferences,
                searchablePreference -> searchablePreference.getKey().equals(key));
    }

    public static Optional<SearchablePreference> findPreferenceById(final Set<SearchablePreference> preferences,
                                                                    final String id) {
        return Sets.findElementByPredicate(
                preferences,
                searchablePreference -> searchablePreference.getId().equals(id));
    }
}
