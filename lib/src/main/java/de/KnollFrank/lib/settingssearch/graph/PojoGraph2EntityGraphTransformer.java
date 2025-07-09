package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.collect.ImmutableSet;

import org.jgrapht.Graph;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenToSearchablePreferenceScreenEntityConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviderData;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviderDatas;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviderFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DetachedSearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.GraphAndDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntities;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphEntity;

public class PojoGraph2EntityGraphTransformer {

    public static GraphAndDbDataProvider toEntityGraph(
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraph,
            final Locale graphId) {
        final Graph<DetachedSearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> transformedGraph =
                GraphTransformerAlgorithm.transform(
                        pojoGraph,
                        SearchablePreferenceEntityEdge.class,
                        createGraphTransformer(graphId));
        final SearchablePreferenceScreenGraphEntity graphEntity = new SearchablePreferenceScreenGraphEntity(graphId);
        return new GraphAndDbDataProvider(
                graphEntity,
                DbDataProviderFactory.createDbDataProvider(
                        DbDataProviderDatas.merge(
                                ImmutableSet
                                        .<DbDataProviderData>builder()
                                        .addAll(getDbDataProviderDatas(transformedGraph.vertexSet()))
                                        .add(
                                                DbDataProviderData
                                                        .builder()
                                                        .withNodesByGraph(
                                                                Map.of(
                                                                        graphEntity,
                                                                        removeDetachedDbDataProviders(transformedGraph).vertexSet()))
                                                        .build())
                                        .build())));
    }

    private static Set<DbDataProviderData> getDbDataProviderDatas(final Set<DetachedSearchablePreferenceScreenEntity> detachedSearchablePreferenceScreenEntities) {
        return detachedSearchablePreferenceScreenEntities
                .stream()
                .map(DetachedSearchablePreferenceScreenEntity::dbDataProviderData)
                .collect(Collectors.toSet());
    }

    private static GraphTransformer<SearchablePreferenceScreen, SearchablePreferenceEdge, DetachedSearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> createGraphTransformer(final Locale graphId) {
        return new GraphTransformer<>() {

            @Override
            public DetachedSearchablePreferenceScreenEntity transformRootNode(final SearchablePreferenceScreen rootNode) {
                return SearchablePreferenceScreenToSearchablePreferenceScreenEntityConverter.toEntity(
                        rootNode,
                        Optional.empty(),
                        graphId);
            }

            @Override
            public DetachedSearchablePreferenceScreenEntity transformInnerNode(final SearchablePreferenceScreen innerNode,
                                                                               final ContextOfInnerNode<SearchablePreferenceEdge, DetachedSearchablePreferenceScreenEntity> contextOfInnerNode) {
                return SearchablePreferenceScreenToSearchablePreferenceScreenEntityConverter.toEntity(
                        innerNode,
                        Optional.of(
                                getPreferenceById(
                                        contextOfInnerNode.transformedParentNode(),
                                        contextOfInnerNode.edgeFromParentNode2InnerNode().preference.getId())),
                        graphId);
            }

            @Override
            public SearchablePreferenceEntityEdge transformEdge(final SearchablePreferenceEdge edge,
                                                                final DetachedSearchablePreferenceScreenEntity transformedParentNode) {
                return new SearchablePreferenceEntityEdge(
                        getPreferenceById(
                                transformedParentNode,
                                edge.preference.getId()));
            }

            private static SearchablePreferenceEntity getPreferenceById(final DetachedSearchablePreferenceScreenEntity screen,
                                                                        final String id) {
                return SearchablePreferenceEntities
                        .findPreferenceById(screen.getAllPreferences(), id)
                        .orElseThrow();
            }
        };
    }

    private static Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> removeDetachedDbDataProviders(final Graph<DetachedSearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> graph) {
        return GraphTransformerAlgorithm.transform(
                graph,
                SearchablePreferenceEntityEdge.class,
                new GraphTransformer<>() {

                    @Override
                    public SearchablePreferenceScreenEntity transformRootNode(final DetachedSearchablePreferenceScreenEntity rootNode) {
                        return rootNode.screen();
                    }

                    @Override
                    public SearchablePreferenceScreenEntity transformInnerNode(final DetachedSearchablePreferenceScreenEntity innerNode,
                                                                               final ContextOfInnerNode<SearchablePreferenceEntityEdge, SearchablePreferenceScreenEntity> contextOfInnerNode) {
                        return innerNode.screen();
                    }

                    @Override
                    public SearchablePreferenceEntityEdge transformEdge(final SearchablePreferenceEntityEdge edge,
                                                                        final SearchablePreferenceScreenEntity transformedParentNode) {
                        return new SearchablePreferenceEntityEdge(edge.preference);
                    }
                });
    }
}
