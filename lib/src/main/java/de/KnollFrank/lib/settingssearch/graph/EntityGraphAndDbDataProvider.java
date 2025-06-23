package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public record EntityGraphAndDbDataProvider(
        Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> entityGraph,
        DbDataProvider dbDataProvider) {
}
