package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.collect.MoreCollectors;

import org.jgrapht.Graph;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenToSearchablePreferenceScreenEntityConverter;
import de.KnollFrank.lib.settingssearch.db.preference.dao.DbDataProviderDatas;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviderData;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviderFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DetachedSearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class PojoGraph2EntityGraphTransformer {

    // FK-TODO: refactor
    public static EntityGraphAndDbDataProvider toEntityGraph(
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraph) {
        final Graph<DetachedSearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> transformedGraph =
                GraphTransformerAlgorithm.transform(
                        pojoGraph,
                        SearchablePreferenceEntityEdge.class,
                        createGraphTransformer());
        final Set<DetachedSearchablePreferenceScreenEntity> detachedSearchablePreferenceScreenEntities = transformedGraph.vertexSet();
        final DbDataProviderData dbDataProviderData = DbDataProviderDatas.merge(getDbDataProviderDatas(detachedSearchablePreferenceScreenEntities));
        return new EntityGraphAndDbDataProvider(
                removeDetachedDbDataProviders(transformedGraph),
                DbDataProviderFactory.createDbDataProvider(dbDataProviderData));
    }

    private static Set<DbDataProviderData> getDbDataProviderDatas(final Set<DetachedSearchablePreferenceScreenEntity> detachedSearchablePreferenceScreenEntities) {
        return detachedSearchablePreferenceScreenEntities
                .stream()
                .map(DetachedSearchablePreferenceScreenEntity::dbDataProviderData)
                .collect(Collectors.toSet());
    }

    private static GraphTransformer<SearchablePreferenceScreen, SearchablePreferenceEdge, DetachedSearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> createGraphTransformer() {
        return new GraphTransformer<>() {

            @Override
            public DetachedSearchablePreferenceScreenEntity transformRootNode(final SearchablePreferenceScreen rootNode) {
                return SearchablePreferenceScreenToSearchablePreferenceScreenEntityConverter.toEntity(
                        rootNode,
                        Optional.empty());
            }

            @Override
            public DetachedSearchablePreferenceScreenEntity transformInnerNode(final SearchablePreferenceScreen innerNode,
                                                                               final ContextOfInnerNode<SearchablePreferenceEdge, DetachedSearchablePreferenceScreenEntity> contextOfInnerNode) {
                return SearchablePreferenceScreenToSearchablePreferenceScreenEntityConverter.toEntity(
                        innerNode,
                        Optional.of(
                                getPreferenceById(
                                        contextOfInnerNode.transformedParentNode(),
                                        contextOfInnerNode.edgeFromParentNode2InnerNode().preference.getId())));
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
                                                                        final int id) {
                return screen
                        .getAllPreferences()
                        .stream()
                        .filter(preference -> preference.getId() == id)
                        .collect(MoreCollectors.onlyElement());
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
