package de.KnollFrank.lib.settingssearch;

import static de.KnollFrank.lib.settingssearch.common.Preferences.getDirectChildren;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;

import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;

public class SearchablePreferenceScreenProvider implements PreferenceScreenProvider {

    private final IsPreferenceSearchable isPreferenceSearchable;

    public SearchablePreferenceScreenProvider(final IsPreferenceSearchable isPreferenceSearchable) {
        this.isPreferenceSearchable = isPreferenceSearchable;
    }

    @Override
    public PreferenceScreen getPreferenceScreen(final PreferenceFragmentCompat preferenceFragment) {
        final PreferenceScreen preferenceScreen = preferenceFragment.getPreferenceScreen();
        removeNonSearchablePreferencesFromPreferenceGroup(preferenceScreen, preferenceFragment);
        return preferenceScreen;
    }

    // FK-TODO: refactor
    // FK-TODO: refine SearchablePreferenceScreenProviderTest to have sub categories with non searchable preferences
    private void removeNonSearchablePreferencesFromPreferenceGroup(final PreferenceGroup preferenceGroup, final PreferenceFragmentCompat preferenceFragment) {
        for (final Preference child : getDirectChildren(preferenceGroup)) {
            if (!isPreferenceSearchable.isPreferenceOfHostSearchable(child, preferenceFragment)) {
                preferenceGroup.removePreference(child);
            } else {
                if (child instanceof final PreferenceGroup childPreferenceGroup) {
                    removeNonSearchablePreferencesFromPreferenceGroup(childPreferenceGroup, preferenceFragment);
                }
            }
        }
    }
}
