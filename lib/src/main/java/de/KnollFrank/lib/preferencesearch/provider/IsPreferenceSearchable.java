package de.KnollFrank.lib.preferencesearch.provider;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

@FunctionalInterface
public interface IsPreferenceSearchable {

    boolean isPreferenceOfHostSearchable(Preference preference, PreferenceFragmentCompat host);
}
