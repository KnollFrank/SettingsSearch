package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import static de.KnollFrank.lib.settingssearch.PreferenceFragmentClassOfActivityTestFactory.createSomePreferenceFragmentClassOfActivity;

import java.util.Optional;
import java.util.Set;

public class SearchablePreferenceScreenTestFactory {

    private SearchablePreferenceScreenTestFactory() {
    }

    public static SearchablePreferenceScreen createScreen(final String id,
                                                          final Set<SearchablePreference> allPreferencesOfPreferenceHierarchy) {
        return new SearchablePreferenceScreen(
                id,
                createSomePreferenceFragmentClassOfActivity(),
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
