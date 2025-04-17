package de.KnollFrank.lib.settingssearch.db.preference.db;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public interface Database {

    void initializeWith(Set<SearchablePreference> preferences);

    boolean isInitialized();

    Set<SearchablePreference> loadAll();

    void persistPreference(SearchablePreference preference);

    void removePreference(int idOfPreference);

    void updateSummary(int idOfPreference, String newSummaryOfPreference);

    int getUnusedId();
}
