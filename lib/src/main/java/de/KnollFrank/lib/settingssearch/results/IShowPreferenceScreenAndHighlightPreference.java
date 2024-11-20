package de.KnollFrank.lib.settingssearch.results;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

@FunctionalInterface
public interface IShowPreferenceScreenAndHighlightPreference {

    void showPreferenceScreenAndHighlightPreference(SearchablePreferencePOJO preference);
}
