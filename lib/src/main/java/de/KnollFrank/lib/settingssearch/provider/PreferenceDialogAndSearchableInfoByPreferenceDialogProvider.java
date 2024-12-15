package de.KnollFrank.lib.settingssearch.provider;

import androidx.fragment.app.Fragment;

import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoByPreferenceDialogProvider;

public record PreferenceDialogAndSearchableInfoByPreferenceDialogProvider<T extends Fragment>(
        T preferenceDialog,
        // FK-TODO: the interface SearchableInfoByPreferenceDialogProvider does not need a T parameter in it's only method, because it is always the above preferenceDialog attribute. Instead move the interface method into this record (after making it an interface)
        SearchableInfoByPreferenceDialogProvider<T> searchableInfoByPreferenceDialogProvider) {
}
