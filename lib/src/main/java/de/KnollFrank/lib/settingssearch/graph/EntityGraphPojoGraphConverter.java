package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.Converter;

public class EntityGraphPojoGraphConverter implements Converter<GraphAndDbDataProvider, Graph<SearchablePreferenceScreen, SearchablePreferenceEdge>> {

    @Override
    public Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> doForward(final GraphAndDbDataProvider graphAndDbDataProvider) {
        return EntityGraph2PojoGraphTransformer.toPojoGraph(
                graphAndDbDataProvider.asGraph(),
                graphAndDbDataProvider.dbDataProvider());
    }

    @Override
    public GraphAndDbDataProvider doBackward(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraph) {
        return PojoGraph2EntityGraphTransformer.toEntityGraph(pojoGraph);
    }
}
