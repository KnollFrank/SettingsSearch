package de.KnollFrank.lib.preferencesearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.fragment.Fragments;

public class PreferenceScreenWithHostProvider {

    private final Fragments fragments;

    public PreferenceScreenWithHostProvider(final Fragments fragments) {
        this.fragments = fragments;
    }

    public Optional<PreferenceScreenWithHost> getPreferenceScreenOfFragment(final String fragment,
                                                                            final Optional<Preference> src) {
        return fragments.instantiateAndInitializeFragment(fragment, src) instanceof final PreferenceFragmentCompat preferenceFragment ?
                Optional.of(PreferenceScreenWithHost.fromPreferenceFragment(preferenceFragment)) :
                Optional.empty();
    }
}