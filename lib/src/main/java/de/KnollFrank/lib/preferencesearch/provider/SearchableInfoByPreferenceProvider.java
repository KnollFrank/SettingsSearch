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
import de.KnollFrank.lib.preferencesearch.fragment.PreferenceDialogs;
import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoByPreferenceDialogProvider;

class SearchableInfoByPreferenceProvider {

    private final PreferenceDialogs preferenceDialogs;
    private final PreferenceDialogProvider preferenceDialogProvider;
    private final SearchableInfoByPreferenceDialogProvider searchableInfoByPreferenceDialogProvider;

    public SearchableInfoByPreferenceProvider(final PreferenceDialogs preferenceDialogs,
                                              final PreferenceDialogProvider preferenceDialogProvider,
                                              final SearchableInfoByPreferenceDialogProvider searchableInfoByPreferenceDialogProvider) {
        this.preferenceDialogs = preferenceDialogs;
        this.preferenceDialogProvider = preferenceDialogProvider;
        this.searchableInfoByPreferenceDialogProvider = searchableInfoByPreferenceDialogProvider;
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
        return Preferences
                .getAllChildren(preferenceScreenWithHost.preferenceScreen)
                .stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                preference ->
                                        preferenceDialogProvider
                                                .getPreferenceDialog(preferenceScreenWithHost.host, preference)
                                                .flatMap(this::getSearchableInfo)));
    }

    // FK-TODO: kein Optional<String>, sondern direkt String?
    private Optional<String> getSearchableInfo(final Fragment preferenceDialog) {
        preferenceDialogs.showPreferenceDialog(preferenceDialog);
        final Optional<String> searchableInfo =
                Optional.of(searchableInfoByPreferenceDialogProvider.getSearchableInfo(preferenceDialog));
        preferenceDialogs.hidePreferenceDialog(preferenceDialog);
        return searchableInfo;
    }
}
