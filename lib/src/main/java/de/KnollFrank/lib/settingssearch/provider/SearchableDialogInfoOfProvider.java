package de.KnollFrank.lib.settingssearch.provider;

import androidx.fragment.app.Fragment;
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

    private <T extends Fragment> String getSearchableDialogInfoOfPreference(final PreferenceDialogAndSearchableInfoByPreferenceDialogProvider<T> data) {
        preferenceDialogs.showPreferenceDialog(data.preferenceDialog());
        final String searchableInfo = data.searchableInfoByPreferenceDialogProvider().getSearchableInfo(data.preferenceDialog());
        preferenceDialogs.hidePreferenceDialog(data.preferenceDialog());
        return searchableInfo;
    }
}
