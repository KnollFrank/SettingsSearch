package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.fragment.Fragments;

public class PreferenceScreenWithHostProvider {

    private final Fragments fragments;
    private final PreferenceScreenProvider preferenceScreenProvider;

    public PreferenceScreenWithHostProvider(final Fragments fragments,
                                            final PreferenceScreenProvider preferenceScreenProvider) {
        this.fragments = fragments;
        this.preferenceScreenProvider = preferenceScreenProvider;
    }

    public Optional<PreferenceScreenWithHost> getPreferenceScreenWithHostOfFragment(
            final String fragment,
            final Optional<PreferenceWithHost> src) {
        return fragments.instantiateAndInitializeFragment(fragment, src) instanceof final PreferenceFragmentCompat preferenceFragment ?
                Optional.of(getPreferenceScreenWithHost(preferenceFragment)) :
                Optional.empty();
    }

    private PreferenceScreenWithHost getPreferenceScreenWithHost(final PreferenceFragmentCompat preferenceFragment) {
        return new PreferenceScreenWithHost(
                preferenceScreenProvider.getPreferenceScreen(preferenceFragment),
                preferenceFragment);
    }
}