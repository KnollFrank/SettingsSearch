package de.KnollFrank.lib.settingssearch.graph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.GraphAndDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.Converter;

public class EntityGraphPojoGraphConverter implements Converter<GraphAndDbDataProvider, SearchablePreferenceScreenTree> {

    @Override
    public SearchablePreferenceScreenTree convertForward(final GraphAndDbDataProvider graphAndDbDataProvider) {
        return new SearchablePreferenceScreenTree(
                EntityGraphToPojoGraphTransformer.toPojoGraph(
                        graphAndDbDataProvider.asGraph(),
                        graphAndDbDataProvider.dbDataProvider()),
                graphAndDbDataProvider.graph().id(),
                graphAndDbDataProvider.graph().configuration());
    }

    @Override
    public GraphAndDbDataProvider convertBackward(final SearchablePreferenceScreenTree pojoGraph) {
        return PojoGraphToEntityGraphTransformer.toEntityGraph(
                pojoGraph.tree(),
                pojoGraph.locale(),
                pojoGraph.configuration());
    }
}
