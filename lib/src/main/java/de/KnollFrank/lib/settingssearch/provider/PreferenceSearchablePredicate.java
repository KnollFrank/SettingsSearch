package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

@FunctionalInterface
public interface PreferenceSearchablePredicate {

    boolean isPreferenceOfHostSearchable(Preference preference, PreferenceFragmentCompat host);
}
