package de.KnollFrank.lib.settingssearch.provider;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

@FunctionalInterface
public interface IncludePreferenceInSearchResultsPredicate {

    boolean includePreferenceInSearchResults(SearchablePreferencePOJO preference);
}
