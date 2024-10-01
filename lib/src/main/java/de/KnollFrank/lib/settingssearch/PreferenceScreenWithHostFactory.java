package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.function.BiPredicate;

import de.KnollFrank.lib.settingssearch.db.SearchablePreferenceTransformer;
import de.KnollFrank.lib.settingssearch.provider.ISearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class PreferenceScreenWithHostFactory {

    public static PreferenceScreenWithHost createSearchablePreferenceScreenWithHost(
            final PreferenceFragmentCompat preferenceFragment,
            final SearchableInfoProvider searchableInfoProvider,
            final ISearchableDialogInfoOfProvider searchableDialogInfoOfProvider,
            final IsPreferenceSearchable isPreferenceSearchable) {
        return new PreferenceScreenWithHost(
                createSearchablePreferenceTransformer(
                        preferenceFragment,
                        searchableInfoProvider,
                        searchableDialogInfoOfProvider,
                        isPreferenceSearchable)
                        .transform2SearchablePreferenceScreen(preferenceFragment.getPreferenceScreen()),
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
