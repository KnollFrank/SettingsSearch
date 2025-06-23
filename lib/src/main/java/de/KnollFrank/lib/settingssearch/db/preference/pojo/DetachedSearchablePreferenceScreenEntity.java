package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.dao.DetachedDbDataProvider;

public record DetachedSearchablePreferenceScreenEntity(
        SearchablePreferenceScreenEntity screen,
        DetachedDbDataProvider detachedDbDataProvider) {

    public Set<SearchablePreferenceEntity> getAllPreferences() {
        return screen.getAllPreferences(detachedDbDataProvider);
    }
}
