package de.KnollFrank.lib.settingssearch.graph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.GraphAndDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.Converter;

public class EntityGraphPojoGraphConverter implements Converter<GraphAndDbDataProvider, SearchablePreferenceScreenGraph> {

    @Override
    public SearchablePreferenceScreenGraph doForward(final GraphAndDbDataProvider graphAndDbDataProvider) {
        return new SearchablePreferenceScreenGraph(
                EntityGraph2PojoGraphTransformer.toPojoGraph(
                        graphAndDbDataProvider.asGraph(),
                        graphAndDbDataProvider.dbDataProvider()),
                graphAndDbDataProvider.graph().id(),
                graphAndDbDataProvider.graph().processed());
    }

    @Override
    public GraphAndDbDataProvider doBackward(final SearchablePreferenceScreenGraph pojoGraph) {
        return PojoGraph2EntityGraphTransformer.toEntityGraph(
                pojoGraph.graph(),
                pojoGraph.locale(),
                pojoGraph.processed());
    }
}
