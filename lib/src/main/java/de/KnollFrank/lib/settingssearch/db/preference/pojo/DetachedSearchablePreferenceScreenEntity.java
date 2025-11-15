package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Set;

public record DetachedSearchablePreferenceScreenEntity(
        SearchablePreferenceScreenEntity screen,
        DbDataProviderData dbDataProviderData) {

    public Set<SearchablePreferenceEntity> getAllPreferencesOfPreferenceHierarchy() {
        return screen.getAllPreferencesOfPreferenceHierarchy(DbDataProviderFactory.createDbDataProvider(dbDataProviderData));
    }
}
