package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.provider.ISearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class PreferenceScreenWithHostProvider {

    private final Fragments fragments;

    public PreferenceScreenWithHostProvider(final Fragments fragments) {
        this.fragments = fragments;
    }

    public Optional<PreferenceScreenWithHost> getPreferenceScreenOfFragment(
            final String fragment,
            final Optional<PreferenceWithHost> src,
            final SearchableInfoProvider searchableInfoProvider,
            final ISearchableDialogInfoOfProvider searchableInfoByPreferenceProvider) {
        return fragments.instantiateAndInitializeFragment(fragment, src) instanceof final PreferenceFragmentCompat preferenceFragment ?
                Optional.of(
                        PreferenceScreenWithHost.fromPreferenceFragment(
                                preferenceFragment,
                                searchableInfoProvider,
                                searchableInfoByPreferenceProvider)) :
                Optional.empty();
    }
}