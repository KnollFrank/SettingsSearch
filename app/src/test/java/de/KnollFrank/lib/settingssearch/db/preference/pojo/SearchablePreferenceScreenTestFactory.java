package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Optional;
import java.util.Set;

public class SearchablePreferenceScreenTestFactory {

    public static SearchablePreferenceScreen createScreen(final String id,
                                                          final Set<SearchablePreference> allPreferencesOfPreferenceHierarchy) {
        return new SearchablePreferenceScreen(
                id,
                null,
                Optional.of("Screen " + id),
                Optional.empty(),
                allPreferencesOfPreferenceHierarchy);
    }
}
