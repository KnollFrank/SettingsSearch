package de.KnollFrank.lib.settingssearch.fragment.navigation;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;

class ConnectedFragmentProvider {

    private final de.KnollFrank.lib.settingssearch.ConnectedFragmentProvider connectedFragmentProvider;
    private final InstantiateAndInitializeFragment instantiateAndInitializeFragment;

    public ConnectedFragmentProvider(final de.KnollFrank.lib.settingssearch.ConnectedFragmentProvider connectedFragmentProvider,
                                     final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        this.connectedFragmentProvider = connectedFragmentProvider;
        this.instantiateAndInitializeFragment = instantiateAndInitializeFragment;
    }

    public Optional<Fragment> getConnectedFragment(final PreferenceFragmentCompat preferenceFragment) {
        return connectedFragmentProvider
                .getConnectedFragment(preferenceFragment.getClass())
                .map(fragment -> instantiateAndInitializeFragment.instantiateAndInitializeFragment(fragment, Optional.empty()));
    }
}
