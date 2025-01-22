package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

@FunctionalInterface
public interface RootPreferenceFragmentOfActivityProvider {

    Optional<Class<? extends PreferenceFragmentCompat>> getRootPreferenceFragmentOfActivity(final String classNameOfActivity);
}
