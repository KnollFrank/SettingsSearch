package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.Sets;

public class SearchablePreferenceScreens {

    private SearchablePreferenceScreens() {
    }

    public static Optional<SearchablePreferenceScreen> findSearchablePreferenceScreenById(
            final Set<SearchablePreferenceScreen> screens,
            final String id) {
        return Sets.findElementByPredicate(
                screens,
                searchablePreferenceScreen -> searchablePreferenceScreen.id().equals(id));
    }
}
