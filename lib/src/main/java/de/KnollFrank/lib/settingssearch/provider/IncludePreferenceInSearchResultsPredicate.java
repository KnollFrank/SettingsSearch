package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

@FunctionalInterface
public interface IncludePreferenceInSearchResultsPredicate {

    boolean includePreferenceOfHostInSearchResults(SearchablePreferencePOJO preference, Class<? extends PreferenceFragmentCompat> host);
}
