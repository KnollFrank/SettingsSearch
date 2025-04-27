package de.KnollFrank.lib.settingssearch.provider;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

@FunctionalInterface
public interface IncludeSearchablePreferencePOJOInSearchResultsPredicate {

    boolean includePreferenceInSearchResults(SearchablePreferencePOJO preference);
}
