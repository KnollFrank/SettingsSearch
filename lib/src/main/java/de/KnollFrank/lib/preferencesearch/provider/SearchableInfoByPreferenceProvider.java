package de.KnollFrank.lib.preferencesearch.provider;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;

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
    private final PreferenceDialogProvider preferenceDialogProvider;

    public SearchableInfoByPreferenceProvider(final Fragments fragments,
                                              final PreferenceDialogProvider preferenceDialogProvider) {
        this.fragments = fragments;
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
                .filter(preference -> preferenceDialogProvider.hasPreferenceDialog(preferenceScreenWithHost.host, preference))
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                preference ->
                                        clickablePreferenceProvider
                                                .asClickablePreference(preference)
                                                .flatMap(clickablePreference ->
                                                        getSearchableInfo(
                                                                preferenceDialogProvider.getPreferenceDialog(
                                                                        preferenceScreenWithHost.host,
                                                                        clickablePreference)))));
    }

    // FK-TODO: kein Optional<String>, sondern direkt String?
    private Optional<String> getSearchableInfo(final Fragment preferenceDialog) {
        fragments.fragmentInitializer.showPreferenceDialog(preferenceDialog);
        final Optional<String> searchableInfo = preferenceDialog instanceof final HasSearchableInfo hasSearchableInfo ?
                Optional.of(hasSearchableInfo.getSearchableInfo()) :
                Optional.empty();
        fragments.fragmentInitializer.hidePreferenceDialog(preferenceDialog);
        return searchableInfo;
    }
}
