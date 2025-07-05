package de.KnollFrank.lib.settingssearch.graph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphEntity;

public record GraphAndDbDataProvider(
        SearchablePreferenceScreenGraphEntity graph,
        DbDataProvider dbDataProvider) {
}
