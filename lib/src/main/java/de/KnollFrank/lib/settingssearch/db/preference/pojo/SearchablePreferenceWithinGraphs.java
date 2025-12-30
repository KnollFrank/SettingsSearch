package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.Sets;

public class SearchablePreferenceWithinGraphs {

    public static Optional<SearchablePreferenceOfHostWithinGraph> findPreferenceByKey(
            final Set<SearchablePreferenceOfHostWithinGraph> preferences,
            final String key) {
        return Sets.findElementByPredicate(
                preferences,
                preference -> preference.searchablePreference().getKey().equals(key));
    }

    public static Optional<SearchablePreferenceOfHostWithinGraph> findPreferenceById(
            final Set<SearchablePreferenceOfHostWithinGraph> preferences,
            final String id) {
        return Sets.findElementByPredicate(
                preferences,
                preference -> preference.searchablePreference().getId().equals(id));
    }
}
