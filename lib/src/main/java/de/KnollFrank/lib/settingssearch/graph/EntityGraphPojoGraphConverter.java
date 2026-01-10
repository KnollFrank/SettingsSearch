package de.KnollFrank.lib.settingssearch.graph;

import static de.KnollFrank.lib.settingssearch.graph.GraphConverterFactory.createGraphConverter;

import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.GraphAndDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.Converter;

public class EntityGraphPojoGraphConverter implements Converter<GraphAndDbDataProvider, SearchablePreferenceScreenGraph> {

    @Override
    public SearchablePreferenceScreenGraph convertForward(final GraphAndDbDataProvider graphAndDbDataProvider) {
        return new SearchablePreferenceScreenGraph(
                new Tree<>(createGraphConverter().toGuava(
                        EntityGraphToPojoGraphTransformer.toPojoGraph(
                                graphAndDbDataProvider.asGraph(),
                                graphAndDbDataProvider.dbDataProvider()))),
                graphAndDbDataProvider.graph().id(),
                graphAndDbDataProvider.graph().configuration());
    }

    @Override
    public GraphAndDbDataProvider convertBackward(final SearchablePreferenceScreenGraph pojoGraph) {
        return PojoGraphToEntityGraphTransformer.toEntityGraph(
                createGraphConverter().toJGraphT(pojoGraph.tree().graph()),
                pojoGraph.locale(),
                pojoGraph.configuration());
    }
}
