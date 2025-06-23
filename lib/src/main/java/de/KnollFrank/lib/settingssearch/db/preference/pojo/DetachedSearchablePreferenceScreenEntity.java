package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Set;

public record DetachedSearchablePreferenceScreenEntity(
        SearchablePreferenceScreenEntity screen,
        DbDataProviderData dbDataProviderData) {

    public Set<SearchablePreferenceEntity> getAllPreferences() {
        return screen.getAllPreferences(DbDataProviderFactory.createDbDataProvider(dbDataProviderData));
    }
}
