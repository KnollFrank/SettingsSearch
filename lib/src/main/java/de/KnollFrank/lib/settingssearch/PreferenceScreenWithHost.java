package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import de.KnollFrank.lib.settingssearch.db.SearchablePreferenceTransformer;
import de.KnollFrank.lib.settingssearch.provider.ISearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public record PreferenceScreenWithHost(PreferenceScreen preferenceScreen,
                                       PreferenceFragmentCompat host) {

    public static PreferenceScreenWithHost fromPreferenceFragment(final PreferenceFragmentCompat preferenceFragment,
                                                                  final SearchableInfoProvider searchableInfoProvider,
                                                                  final ISearchableDialogInfoOfProvider searchableDialogInfoOfProvider) {
        return new PreferenceScreenWithHost(
                new SearchablePreferenceTransformer(
                        preferenceFragment.getPreferenceManager(),
                        searchableInfoProvider,
                        preferenceFragment,
                        searchableDialogInfoOfProvider)
                        .transform2SearchablePreferenceScreen(preferenceFragment.getPreferenceScreen()),
                preferenceFragment);
    }
}
