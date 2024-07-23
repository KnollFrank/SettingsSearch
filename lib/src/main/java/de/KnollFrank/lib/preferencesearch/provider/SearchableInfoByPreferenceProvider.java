package de.KnollFrank.lib.preferencesearch.provider;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.preferencesearch.common.Maps;
import de.KnollFrank.lib.preferencesearch.common.Preferences;
import de.KnollFrank.lib.preferencesearch.fragment.Fragments;
import de.KnollFrank.lib.preferencesearch.search.provider.HasSearchableInfo;

class SearchableInfoByPreferenceProvider {

    private final Fragments fragments;
    private final FragmentManager fragmentManager;
    private final PreferenceDialogProvider preferenceDialogProvider;

    public SearchableInfoByPreferenceProvider(final Fragments fragments,
                                              final FragmentManager fragmentManager,
                                              final PreferenceDialogProvider preferenceDialogProvider) {
        this.fragments = fragments;
        this.fragmentManager = fragmentManager;
        this.preferenceDialogProvider = preferenceDialogProvider;
    }

    public Map<Preference, String> getSearchableInfoByPreference(
            final Collection<PreferenceScreenWithHost> preferenceScreenWithHostList) {
        return Maps.merge(
                preferenceScreenWithHostList
                        .stream()
                        .map(this::getSearchableInfoByPreference)
                        .collect(Collectors.toList()));
    }

    private Map<Preference, String> getSearchableInfoByPreference(
            final PreferenceScreenWithHost preferenceScreenWithHost) {
        return Maps.filterPresentValues(
                getOptionalSearchableInfoByPreference(
                        preferenceScreenWithHost));
    }

    private Map<Preference, Optional<String>> getOptionalSearchableInfoByPreference(
            final PreferenceScreenWithHost preferenceScreenWithHost) {
        final ClickablePreferenceProvider clickablePreferenceProvider =
                ClickablePreferenceProvider.create(
                        preferenceScreenWithHost.host,
                        fragments::instantiateAndInitializeFragment);
        return Preferences
                .getAllChildren(preferenceScreenWithHost.preferenceScreen)
                .stream()
                .filter(preference -> preferenceDialogProvider.hasFragment(preferenceScreenWithHost.host, preference))
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                preference ->
                                        clickablePreferenceProvider
                                                .asClickablePreference(preference)
                                                .flatMap(clickablePreference -> getSearchableInfo(preferenceScreenWithHost.host, clickablePreference))));
    }

    private Optional<String> getSearchableInfo(final Class<? extends PreferenceFragmentCompat> host,
                                               final Preference preference) {
        final DialogFragments dialogFragments =
                new DialogFragments(
                        fragmentManager,
                        fragmentManager -> preferenceDialogProvider.getFragment(host, preference, fragmentManager));
        return dialogFragments.getStringFromDialogFragment(preference, this::getSearchableInfo);
    }

    private Optional<String> getSearchableInfo(final Fragment fragment) {
        return fragment instanceof final HasSearchableInfo hasSearchableInfo ?
                Optional.of(hasSearchableInfo.getSearchableInfo()) :
                Optional.empty();
    }
}
