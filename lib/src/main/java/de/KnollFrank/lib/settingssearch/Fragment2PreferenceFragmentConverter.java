package de.KnollFrank.lib.settingssearch;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

@FunctionalInterface
public interface Fragment2PreferenceFragmentConverter {

    Optional<Class<? extends PreferenceFragmentCompat>> asPreferenceFragment(Class<? extends Fragment> fragment);
}
