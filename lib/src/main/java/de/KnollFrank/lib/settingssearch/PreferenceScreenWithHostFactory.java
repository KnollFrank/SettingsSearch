package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.SearchablePreferenceTransformer;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;

public class PreferenceScreenWithHostFactory {

    public static SearchablePreferenceScreenWithMapAndHost createSearchablePreferenceScreenWithHost(
            final PreferenceFragmentCompat preferenceFragment,
            final IsPreferenceSearchable isPreferenceSearchable,
            final SearchableInfoAndDialogInfoProvider searchableInfoAndDialogInfoProvider) {
        final SearchablePreferenceTransformer transformer =
                new SearchablePreferenceTransformer(
                        preferenceFragment.getPreferenceManager(),
                        preferenceFragment,
                        isPreferenceSearchable,
                        searchableInfoAndDialogInfoProvider);
        return new SearchablePreferenceScreenWithMapAndHost(
                transformer.transform2SearchablePreferenceScreen(preferenceFragment.getPreferenceScreen()),
                preferenceFragment);
    }

    public static PreferenceScreenWithHost createPreferenceScreenWithHost(final PreferenceFragmentCompat preferenceFragment) {
        return new PreferenceScreenWithHost(
                preferenceFragment.getPreferenceScreen(),
                preferenceFragment);
    }
}
