package de.KnollFrank.lib.preferencesearch;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.fragment.Fragments;

public class PreferenceScreenWithHostProvider {

    private final Fragments fragments;

    public PreferenceScreenWithHostProvider(final Fragments fragments) {
        this.fragments = fragments;
    }

    public Optional<PreferenceScreenWithHost> getPreferenceScreenOfFragment(final String fragment) {
        final Fragment _fragment = fragments.instantiateAndInitializeFragment(fragment);
        return _fragment instanceof final PreferenceFragmentCompat preferenceFragment ?
                Optional.of(PreferenceScreenWithHost.fromPreferenceFragment(preferenceFragment)) :
                Optional.empty();
    }
}