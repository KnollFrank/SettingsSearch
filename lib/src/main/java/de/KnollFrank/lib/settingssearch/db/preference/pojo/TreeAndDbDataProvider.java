package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import com.google.common.graph.ImmutableValueGraph;

import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenEntitiesToGraphConverter;

public record TreeAndDbDataProvider(
        SearchablePreferenceScreenTreeEntity tree,
        DbDataProvider dbDataProvider) {

    @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
    public Tree<SearchablePreferenceScreenEntity, SearchablePreferenceEntity, ImmutableValueGraph<SearchablePreferenceScreenEntity, SearchablePreferenceEntity>> asGraph() {
        return SearchablePreferenceScreenEntitiesToGraphConverter.convertScreensToTree(
                tree.getNodes(dbDataProvider),
                dbDataProvider);
    }
}
