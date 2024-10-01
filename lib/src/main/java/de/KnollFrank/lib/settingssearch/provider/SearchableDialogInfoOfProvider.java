package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogs;

public class SearchableDialogInfoOfProvider implements ISearchableDialogInfoOfProvider{

    private final PreferenceDialogs preferenceDialogs;
    private final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider;

    public SearchableDialogInfoOfProvider(final PreferenceDialogs preferenceDialogs,
                                          final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider) {
        this.preferenceDialogs = preferenceDialogs;
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
    }

    @Override
    public Optional<String> getSearchableDialogInfoOfPreference(final Preference preference, final PreferenceFragmentCompat hostOfPreference) {
        return preferenceDialogAndSearchableInfoProvider
                .getPreferenceDialogAndSearchableInfoByPreferenceDialogProvider(preference, hostOfPreference)
                .map(this::getSearchableDialogInfoOfPreference);
    }

    private String getSearchableDialogInfoOfPreference(final PreferenceDialogAndSearchableInfoByPreferenceDialogProvider data) {
        preferenceDialogs.showPreferenceDialog(data.preferenceDialog());
        final String searchableInfo = data.searchableInfoByPreferenceDialogProvider().getSearchableInfo(data.preferenceDialog());
        preferenceDialogs.hidePreferenceDialog(data.preferenceDialog());
        return searchableInfo;
    }
}
