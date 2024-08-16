package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

@FunctionalInterface
public interface IsPreferenceSearchable {

    boolean isPreferenceOfHostSearchable(Preference preference, PreferenceFragmentCompat host);
}
