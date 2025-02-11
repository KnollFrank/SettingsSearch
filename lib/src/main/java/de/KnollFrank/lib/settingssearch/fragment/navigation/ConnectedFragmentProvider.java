package de.KnollFrank.lib.settingssearch.fragment.navigation;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;

class ConnectedFragmentProvider {

    private final Map<Class<? extends PreferenceFragmentCompat>, Class<? extends Fragment>> fragmentsConnected2PreferenceFragments;
    private final InstantiateAndInitializeFragment instantiateAndInitializeFragment;

    public ConnectedFragmentProvider(final Map<Class<? extends PreferenceFragmentCompat>, Class<? extends Fragment>> fragmentsConnected2PreferenceFragments,
                                     final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        this.fragmentsConnected2PreferenceFragments = fragmentsConnected2PreferenceFragments;
        this.instantiateAndInitializeFragment = instantiateAndInitializeFragment;
    }

    public Optional<Fragment> getConnectedFragment(final PreferenceFragmentCompat preferenceFragment) {
        return Maps
                .get(fragmentsConnected2PreferenceFragments, preferenceFragment.getClass())
                .map(fragment -> instantiateAndInitializeFragment.instantiateAndInitializeFragment(fragment, Optional.empty()));
    }
}
