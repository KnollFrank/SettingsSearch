package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

@FunctionalInterface
public interface PreferenceSearchablePredicate {

    boolean isPreferenceSearchable(Preference preference, PreferenceFragmentCompat hostOfPreference);
}
