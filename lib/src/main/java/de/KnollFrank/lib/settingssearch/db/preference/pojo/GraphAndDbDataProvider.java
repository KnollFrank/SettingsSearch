package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenEntitiesToGraphConverter;

public record GraphAndDbDataProvider(
        SearchablePreferenceScreenGraphEntity graph,
        DbDataProvider dbDataProvider) {

    public Tree<SearchablePreferenceScreenEntity, SearchablePreferenceEntity> asGraph() {
        return SearchablePreferenceScreenEntitiesToGraphConverter.convertScreensToTree(
                graph.getNodes(dbDataProvider),
                dbDataProvider);
    }
}
