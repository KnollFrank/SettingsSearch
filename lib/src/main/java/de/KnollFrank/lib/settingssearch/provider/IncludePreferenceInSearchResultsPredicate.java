package de.KnollFrank.lib.settingssearch.provider;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;

@FunctionalInterface
public interface IncludePreferenceInSearchResultsPredicate {

    boolean includePreferenceInSearchResults(SearchablePreferenceEntity preference);
}
