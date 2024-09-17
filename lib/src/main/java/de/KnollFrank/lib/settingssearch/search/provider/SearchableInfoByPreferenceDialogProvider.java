package de.KnollFrank.lib.settingssearch.search.provider;

import androidx.fragment.app.Fragment;

@FunctionalInterface
public interface SearchableInfoByPreferenceDialogProvider<T extends Fragment> {

    String getSearchableInfo(T preferenceDialog);
}
