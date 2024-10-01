package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;
import java.util.function.BiPredicate;

import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.provider.ISearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;
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
            final ISearchableDialogInfoOfProvider searchableInfoByPreferenceProvider,
            final IsPreferenceSearchable isPreferenceSearchable) {
        return fragments.instantiateAndInitializeFragment(fragment, src) instanceof final PreferenceFragmentCompat preferenceFragment ?
                Optional.of(
                        PreferenceScreenWithHostFactory.createSearchablePreferenceScreenWithHost(
                                preferenceFragment,
                                searchableInfoProvider,
                                searchableInfoByPreferenceProvider,
                                isPreferenceSearchable)) :
                Optional.empty();
    }
}