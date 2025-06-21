package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviders;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public record EntityGraphAndDbDataProviders(
        Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> entityGraph,
        DbDataProviders dbDataProviders) {
}
