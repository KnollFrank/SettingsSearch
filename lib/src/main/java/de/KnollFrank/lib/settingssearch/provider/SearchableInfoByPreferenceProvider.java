package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.Preference;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogs;

class SearchableInfoByPreferenceProvider {

    private final PreferenceDialogs preferenceDialogs;
    private final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider;

    public SearchableInfoByPreferenceProvider(final PreferenceDialogs preferenceDialogs,
                                              final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider) {
        this.preferenceDialogs = preferenceDialogs;
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
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
                                        preferenceDialogAndSearchableInfoProvider
                                                .getPreferenceDialogAndSearchableInfoByPreferenceDialogProvider(preferenceScreenWithHost.host, preference)
                                                .map(this::getSearchableInfo)));
    }

    private String getSearchableInfo(final PreferenceDialogAndSearchableInfoByPreferenceDialogProvider data) {
        preferenceDialogs.showPreferenceDialog(data.preferenceDialog);
        final String searchableInfo = data.searchableInfoByPreferenceDialogProvider.getSearchableInfo(data.preferenceDialog);
        preferenceDialogs.hidePreferenceDialog(data.preferenceDialog);
        return searchableInfo;
    }
}
