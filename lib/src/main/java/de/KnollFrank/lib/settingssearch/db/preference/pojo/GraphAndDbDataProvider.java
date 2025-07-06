package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenEntitiesToGraphConverter;

public record GraphAndDbDataProvider(
        SearchablePreferenceScreenGraphEntity graph,
        DbDataProvider dbDataProvider) {

    public Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> asGraph() {
        return SearchablePreferenceScreenEntitiesToGraphConverter.convertScreensToGraph(
                graph.getNodes(dbDataProvider),
                dbDataProvider);
    }
}
