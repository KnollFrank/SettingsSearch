package de.KnollFrank.lib.settingssearch;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

public interface Fragment2PreferenceFragmentConverter {

    Optional<PreferenceFragmentCompat> asPreferenceFragment(Fragment fragment);

    Optional<Class<? extends PreferenceFragmentCompat>> asPreferenceFragment(final Class<? extends Fragment> fragment);
}
