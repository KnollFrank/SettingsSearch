package de.KnollFrank.lib.settingssearch.provider;

import androidx.fragment.app.Fragment;

import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoByPreferenceDialogProvider;

public record PreferenceDialogAndSearchableInfoByPreferenceDialogProvider<T extends Fragment>(
        T preferenceDialog,
        SearchableInfoByPreferenceDialogProvider<T> searchableInfoByPreferenceDialogProvider) {
}
