package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import de.KnollFrank.lib.settingssearch.db.preference.dao.DetachedDbDataProvider;

public record DetachedSearchablePreferenceEntity(
        SearchablePreferenceEntity preference,
        DetachedDbDataProvider detachedDbDataProvider) {
}
