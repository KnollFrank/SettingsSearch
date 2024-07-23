package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.fragment.app.Fragment;

// FK-TODO: rename
@FunctionalInterface
public interface SearchableInfoByPreferenceDialogProvider {

    String getSearchableInfo(Fragment preferenceDialog);
}
