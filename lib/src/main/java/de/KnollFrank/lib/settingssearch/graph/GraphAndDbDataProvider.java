package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphEntity;

public record GraphAndDbDataProvider(
        SearchablePreferenceScreenGraphEntity graph,
        DbDataProvider dbDataProvider) {

    public Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> asGraph() {
        return SearchablePreferenceScreenEntitiesToGraphConverter.convertScreensToGraph(
                graph.getNodes(dbDataProvider),
                dbDataProvider);
    }
}
