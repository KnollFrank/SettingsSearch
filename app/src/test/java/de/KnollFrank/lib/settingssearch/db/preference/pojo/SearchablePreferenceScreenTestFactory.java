package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;
import java.util.Set;

public class SearchablePreferenceScreenTestFactory {

    public static SearchablePreferenceScreen createScreen(final String id,
                                                          final Set<SearchablePreference> allPreferencesOfPreferenceHierarchy) {
        return new SearchablePreferenceScreen(
                id,
                PreferenceFragmentCompat.class,
                Optional.of("Screen " + id),
                Optional.empty(),
                allPreferencesOfPreferenceHierarchy);
    }

    public static SearchablePreferenceScreen createScreen(final SearchablePreference searchablePreference) {
        return createScreen(
                searchablePreference.getKey(),
                Set.of(searchablePreference));
    }
}
