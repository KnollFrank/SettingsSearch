package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;

import java.util.function.Predicate;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;

public class SearchablePreferenceScreenProvider implements PreferenceScreenProvider {

    private final PreferenceSearchablePredicate preferenceSearchablePredicate;

    public SearchablePreferenceScreenProvider(final PreferenceSearchablePredicate preferenceSearchablePredicate) {
        this.preferenceSearchablePredicate = preferenceSearchablePredicate;
    }

    @Override
    public PreferenceScreen getPreferenceScreen(final PreferenceFragmentCompat preferenceFragment) {
        final PreferenceScreen preferenceScreen = preferenceFragment.getPreferenceScreen();
        removeNonSearchablePreferencesFromPreferenceScreen(preferenceScreen, preferenceFragment);
        return preferenceScreen;
    }

    private void removeNonSearchablePreferencesFromPreferenceScreen(final PreferenceScreen preferenceScreen,
                                                                    final PreferenceFragmentCompat preferenceFragment) {
        final Predicate<Preference> isPreferenceNonSearchable =
                preference ->
                        !preferenceSearchablePredicate.isPreferenceSearchable(
                                preference,
                                preferenceFragment);
        removePreferencesFromPreferenceGroup(preferenceScreen, isPreferenceNonSearchable);
    }

    private static void removePreferencesFromPreferenceGroup(final PreferenceGroup preferenceGroup,
                                                             final Predicate<Preference> shallRemovePreference) {
        for (final Preference child : Preferences.getImmediateChildren(preferenceGroup)) {
            if (shallRemovePreference.test(child)) {
                preferenceGroup.removePreference(child);
            } else if (child instanceof final PreferenceGroup childPreferenceGroup) {
                removePreferencesFromPreferenceGroup(childPreferenceGroup, shallRemovePreference);
            }
        }
    }
}
