package de.KnollFrank.lib.settingssearch;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.fragment.Fragments;

public class PreferenceScreenWithHostProvider {

    private final Fragments fragments;
    private final PreferenceScreenProvider preferenceScreenProvider;
    private final Fragment2PreferenceFragmentConverter fragment2PreferenceFragmentConverter;

    public PreferenceScreenWithHostProvider(final Fragments fragments,
                                            final PreferenceScreenProvider preferenceScreenProvider,
                                            final Fragment2PreferenceFragmentConverter fragment2PreferenceFragmentConverter) {
        this.fragments = fragments;
        this.preferenceScreenProvider = preferenceScreenProvider;
        this.fragment2PreferenceFragmentConverter = fragment2PreferenceFragmentConverter;
    }

    public Optional<PreferenceScreenWithHost> getPreferenceScreenWithHostOfFragment(
            final String fragment,
            final Optional<PreferenceWithHost> src) {
        return this
                .getPreferenceFragment(fragment, src)
                .map(this::getPreferenceScreenWithHost);
    }

    private Optional<PreferenceFragmentCompat> getPreferenceFragment(final String fragment, final Optional<PreferenceWithHost> src) {
        final Fragment _fragment = fragments.instantiateAndInitializeFragment(fragment, src);
        return _fragment instanceof final PreferenceFragmentCompat preferenceFragment ?
                Optional.of(preferenceFragment) :
                fragment2PreferenceFragmentConverter.asPreferenceFragment(_fragment);
    }

    private PreferenceScreenWithHost getPreferenceScreenWithHost(final PreferenceFragmentCompat preferenceFragment) {
        return new PreferenceScreenWithHost(
                preferenceScreenProvider.getPreferenceScreen(preferenceFragment),
                preferenceFragment);
    }
}