package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.Sets;

public class SearchablePreferenceWithinTrees {

    private SearchablePreferenceWithinTrees() {
    }

    public static Optional<SearchablePreferenceOfHostWithinTree> findPreferenceByKey(
            final Set<SearchablePreferenceOfHostWithinTree> preferences,
            final String key) {
        return Sets.findElementByPredicate(
                preferences,
                preference -> preference.searchablePreference().getKey().equals(key));
    }

    public static Optional<SearchablePreferenceOfHostWithinTree> findPreferenceById(
            final Set<SearchablePreferenceOfHostWithinTree> preferences,
            final String id) {
        return Sets.findElementByPredicate(
                preferences,
                preference -> preference.searchablePreference().getId().equals(id));
    }
}
