package de.KnollFrank.lib.settingssearch.results;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

@FunctionalInterface
public interface IShowPreferenceScreenAndHighlightPreference {

    void showPreferenceScreenAndHighlightPreference(SearchablePreference preference, int startNavigationAtIndexWithinPreferencePath);
}
