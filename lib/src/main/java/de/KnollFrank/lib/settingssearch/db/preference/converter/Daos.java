package de.KnollFrank.lib.settingssearch.db.preference.converter;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public record Daos(SearchablePreferenceScreenEntity.DbDataProvider screenDao,
                   SearchablePreferenceEntity.DbDataProvider preferenceDao) {
}
