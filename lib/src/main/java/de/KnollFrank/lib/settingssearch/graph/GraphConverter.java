package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.Converter;

// Fk-TODO: flip arguments of Converter
public class GraphConverter implements Converter<EntityGraphAndDbDataProvider, Graph<SearchablePreferenceScreen, SearchablePreferenceEdge>> {

    @Override
    public Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> doForward(final EntityGraphAndDbDataProvider entityGraphAndDbDataProvider) {
        return EntityGraph2PojoGraphTransformer.toPojoGraph(
                entityGraphAndDbDataProvider.entityGraph(),
                entityGraphAndDbDataProvider.dbDataProvider());
    }

    @Override
    public EntityGraphAndDbDataProvider doBackward(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraph) {
        return PojoGraph2EntityGraphTransformer.toEntityGraph(pojoGraph);
    }
}
