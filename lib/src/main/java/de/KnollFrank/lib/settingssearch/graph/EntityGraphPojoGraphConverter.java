package de.KnollFrank.lib.settingssearch.graph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.Converter;

public class EntityGraphPojoGraphConverter implements Converter<GraphAndDbDataProvider, GraphForLocale> {

    @Override
    public GraphForLocale doForward(final GraphAndDbDataProvider graphAndDbDataProvider) {
        return new GraphForLocale(
                EntityGraph2PojoGraphTransformer.toPojoGraph(
                        graphAndDbDataProvider.asGraph(),
                        graphAndDbDataProvider.dbDataProvider()),
                graphAndDbDataProvider.graph().locale());
    }

    @Override
    public GraphAndDbDataProvider doBackward(final GraphForLocale pojoGraph) {
        return PojoGraph2EntityGraphTransformer.toEntityGraph(
                pojoGraph.graph(),
                pojoGraph.locale());
    }
}
