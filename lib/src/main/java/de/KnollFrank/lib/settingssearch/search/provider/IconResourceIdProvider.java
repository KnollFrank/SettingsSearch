package de.KnollFrank.lib.settingssearch.search.provider;

import androidx.annotation.IdRes;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

@FunctionalInterface
public interface IconResourceIdProvider {

    @IdRes
    Optional<Integer> getIconResourceIdOfPreference(Preference preference, PreferenceFragmentCompat hostOfPreference);
}
