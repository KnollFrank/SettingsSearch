package de.KnollFrank.lib.settingssearch;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;

public class PreferenceScreenWithHostProvider {

    private final InstantiateAndInitializeFragment instantiateAndInitializeFragment;
    private final PreferenceScreenProvider preferenceScreenProvider;
    private final Fragment2PreferenceFragmentConverter fragment2PreferenceFragmentConverter;

    public PreferenceScreenWithHostProvider(final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
                                            final PreferenceScreenProvider preferenceScreenProvider,
                                            final Fragment2PreferenceFragmentConverter fragment2PreferenceFragmentConverter) {
        this.instantiateAndInitializeFragment = instantiateAndInitializeFragment;
        this.preferenceScreenProvider = preferenceScreenProvider;
        this.fragment2PreferenceFragmentConverter = fragment2PreferenceFragmentConverter;
    }

    public Optional<PreferenceScreenWithHost> getPreferenceScreenWithHostOfFragment(
            final Class<? extends Fragment> fragmentClass,
            final Optional<PreferenceWithHost> src) {
        return this
                .getPreferenceFragment(fragmentClass, src)
                .map(this::getPreferenceScreenWithHost);
    }

    private Optional<? extends PreferenceFragmentCompat> getPreferenceFragment(
            final Class<? extends Fragment> fragmentClass,
            final Optional<PreferenceWithHost> src) {
        final Fragment _fragment = instantiateAndInitializeFragment.instantiateAndInitializeFragment(fragmentClass, src);
        return _fragment instanceof final PreferenceFragmentCompat preferenceFragment ?
                Optional.of(preferenceFragment) :
                fragment2PreferenceFragmentConverter
                        .asPreferenceFragment(_fragment.getClass())
                        .map(preferenceFragment -> instantiateAndInitializeFragment.instantiateAndInitializeFragment(preferenceFragment, Optional.empty()));
    }

    private PreferenceScreenWithHost getPreferenceScreenWithHost(final PreferenceFragmentCompat preferenceFragment) {
        return new PreferenceScreenWithHost(
                preferenceScreenProvider.getPreferenceScreen(preferenceFragment),
                preferenceFragment);
    }
}