package de.KnollFrank.lib.settingssearch.provider;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

@FunctionalInterface
public interface IncludeSearchablePreferencePOJOInSearchResultsPredicate {

    boolean includePreferenceInSearchResults(SearchablePreference preference);
}
