package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenToSearchablePreferenceScreenEntityConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceToSearchablePreferenceEntityConverter;
import de.KnollFrank.lib.settingssearch.db.preference.dao.DetachedDbDataProviderBuilder;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviders;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class PojoGraph2EntityGraphTransformer {

    public static EntityGraphAndDbDataProviders toEntityGraph(
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraph) {
        return new EntityGraphAndDbDataProviders(
                GraphTransformerAlgorithm.transform(
                        pojoGraph,
                        SearchablePreferenceEntityEdge.class,
                        createGraphTransformer()),
                new DbDataProviders(
                        new DetachedDbDataProviderBuilder().createDetachedDbDataProvider(),
                        new DetachedDbDataProviderBuilder().createDetachedDbDataProvider()));
    }

    private static GraphTransformer<SearchablePreferenceScreen, SearchablePreferenceEdge, SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> createGraphTransformer() {
        return new GraphTransformer<>() {

            @Override
            public SearchablePreferenceScreenEntity transformRootNode(final SearchablePreferenceScreen rootNode) {
                return SearchablePreferenceScreenToSearchablePreferenceScreenEntityConverter.toEntity(rootNode);
            }

            @Override
            public SearchablePreferenceScreenEntity transformInnerNode(final SearchablePreferenceScreen innerNode, final ContextOfInnerNode<SearchablePreferenceEdge, SearchablePreferenceScreenEntity> contextOfInnerNode) {
                return SearchablePreferenceScreenToSearchablePreferenceScreenEntityConverter.toEntity(innerNode);
            }

            @Override
            public SearchablePreferenceEntityEdge transformEdge(final SearchablePreferenceEdge edge, final SearchablePreferenceScreenEntity transformedParentNode) {
                // FK-TODO: analog zu EntityGraph2PojoGraphTransformer.transformEdge() die Daten nicht berechnen, sondern nur auslesen.
                return new SearchablePreferenceEntityEdge(
                        SearchablePreferenceToSearchablePreferenceEntityConverter.toEntity(
                                edge.preference,
                                // FK-FIXME: not correct: Optional.empty()
                                Optional.empty(),
                                transformedParentNode.getId()));
            }
        };
    }
}
