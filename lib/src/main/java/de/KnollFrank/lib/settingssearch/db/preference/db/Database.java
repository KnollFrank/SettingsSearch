package de.KnollFrank.lib.settingssearch.db.preference.db;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public interface Database {

    void persist(Set<SearchablePreference> preferences);

    Set<SearchablePreference> load();

    boolean isInitialized();
}
