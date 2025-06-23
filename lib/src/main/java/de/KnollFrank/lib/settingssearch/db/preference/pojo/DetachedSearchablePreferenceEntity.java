package de.KnollFrank.lib.settingssearch.db.preference.pojo;

public record DetachedSearchablePreferenceEntity(
        SearchablePreferenceEntity preference,
        DbDataProviderData dbDataProviderData) {
}
