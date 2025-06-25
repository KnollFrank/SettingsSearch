package de.KnollFrank.lib.settingssearch.provider;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

@FunctionalInterface
public interface IncludePreferenceInSearchResultsPredicate {

    boolean includePreferenceInSearchResults(SearchablePreference preference);
}
