package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;

public class SearchablePreferenceScreenProvider implements PreferenceScreenProvider {

    private final IsPreferenceSearchable isPreferenceSearchable;

    public SearchablePreferenceScreenProvider(final IsPreferenceSearchable isPreferenceSearchable) {
        this.isPreferenceSearchable = isPreferenceSearchable;
    }

    @Override
    public PreferenceScreen getPreferenceScreen(final PreferenceFragmentCompat preferenceFragment) {
        return preferenceFragment.getPreferenceScreen();
    }
}
