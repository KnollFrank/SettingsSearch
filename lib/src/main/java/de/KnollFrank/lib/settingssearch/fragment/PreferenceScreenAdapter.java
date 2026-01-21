package de.KnollFrank.lib.settingssearch.fragment;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;

import java.util.function.Predicate;

import de.KnollFrank.lib.settingssearch.common.Functions;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;

class PreferenceScreenAdapter {

    public static void removeNonSearchablePreferencesFromPreferenceScreenOfPreferenceFragment(
            final PreferenceFragmentCompat preferenceFragment,
            final PreferenceSearchablePredicate preferenceSearchablePredicate) {
        final Predicate<Preference> isPreferenceNonSearchable =
                Functions
                        .apply(
                                Functions.swap(preferenceSearchablePredicate::isPreferenceSearchable),
                                preferenceFragment)
                        .negate();
        removePreferencesFromPreferenceGroup(preferenceFragment.getPreferenceScreen(), isPreferenceNonSearchable);
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
