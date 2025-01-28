package de.KnollFrank.lib.settingssearch.provider;

import androidx.fragment.app.Fragment;

import java.util.Optional;

@FunctionalInterface
public interface RootPreferenceFragmentOfActivityProvider {

    Optional<Class<? extends Fragment>> getRootPreferenceFragmentOfActivity(final String classNameOfActivity);
}
