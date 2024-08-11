package de.KnollFrank.lib.preferencesearch.provider;

import androidx.fragment.app.Fragment;

import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoByPreferenceDialogProvider;

public class PreferenceDialogAndSearchableInfoByPreferenceDialogProvider {

    public final Fragment preferenceDialog;
    public final SearchableInfoByPreferenceDialogProvider searchableInfoByPreferenceDialogProvider;

    public PreferenceDialogAndSearchableInfoByPreferenceDialogProvider(
            final Fragment preferenceDialog,
            final SearchableInfoByPreferenceDialogProvider searchableInfoByPreferenceDialogProvider) {
        this.preferenceDialog = preferenceDialog;
        this.searchableInfoByPreferenceDialogProvider = searchableInfoByPreferenceDialogProvider;
    }
}