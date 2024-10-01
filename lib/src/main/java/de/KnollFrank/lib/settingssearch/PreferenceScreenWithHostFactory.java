package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.lib.settingssearch.db.SearchablePreferenceTransformer;
import de.KnollFrank.lib.settingssearch.provider.ISearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class PreferenceScreenWithHostFactory {

    public static SearchablePreferenceScreenWithHost createSearchablePreferenceScreenWithHost(
            final PreferenceFragmentCompat preferenceFragment,
            final SearchableInfoProvider searchableInfoProvider,
            final ISearchableDialogInfoOfProvider searchableDialogInfoOfProvider,
            final IsPreferenceSearchable isPreferenceSearchable) {
        return new SearchablePreferenceScreenWithHost(
                createSearchablePreferenceTransformer(
                        preferenceFragment,
                        searchableInfoProvider,
                        searchableDialogInfoOfProvider,
                        isPreferenceSearchable)
                        .transform2SearchablePreferenceScreen(preferenceFragment.getPreferenceScreen()),
                preferenceFragment);
    }

    public static PreferenceScreenWithHost createPreferenceScreenWithHost(final PreferenceFragmentCompat preferenceFragment) {
        return new PreferenceScreenWithHost(
                preferenceFragment.getPreferenceScreen(),
                preferenceFragment);
    }

    private static SearchablePreferenceTransformer createSearchablePreferenceTransformer(
            final PreferenceFragmentCompat preferenceFragment,
            final SearchableInfoProvider searchableInfoProvider,
            final ISearchableDialogInfoOfProvider searchableDialogInfoOfProvider,
            final IsPreferenceSearchable isPreferenceSearchable) {
        return new SearchablePreferenceTransformer(
                preferenceFragment.getPreferenceManager(),
                searchableInfoProvider,
                preferenceFragment,
                searchableDialogInfoOfProvider,
                isPreferenceSearchable);
    }
}
