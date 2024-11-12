package de.KnollFrank.lib.settingssearch.search.provider;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

@FunctionalInterface
public interface IconResourceIdProvider {

    Optional<Integer> getIconResourceIdOfPreference(Preference preference, PreferenceFragmentCompat hostOfPreference);
}
