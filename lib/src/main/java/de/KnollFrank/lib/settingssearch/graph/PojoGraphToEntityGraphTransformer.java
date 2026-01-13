package de.KnollFrank.lib.settingssearch.graph;

import android.os.PersistableBundle;

import com.google.common.collect.ImmutableSet;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraph;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.common.graph.TreeTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.TreeTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenToSearchablePreferenceScreenEntityConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviderData;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviderDatas;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviderFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DetachedSearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.GraphAndDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntities;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphEntity;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class PojoGraphToEntityGraphTransformer {

    public static GraphAndDbDataProvider toEntityGraph(
            final Tree<SearchablePreferenceScreen, SearchablePreference, ? extends ValueGraph<SearchablePreferenceScreen, SearchablePreference>> pojoGraph,
            final Locale graphId,
            final PersistableBundle configuration) {
        final Tree<DetachedSearchablePreferenceScreenEntity, SearchablePreferenceEntity, ImmutableValueGraph<DetachedSearchablePreferenceScreenEntity, SearchablePreferenceEntity>> transformedGraph =
                TreeTransformerAlgorithm.transform(
                        pojoGraph,
                        createGraphTransformer(graphId));
        final SearchablePreferenceScreenGraphEntity graphEntity = new SearchablePreferenceScreenGraphEntity(graphId, configuration);
        return new GraphAndDbDataProvider(
                graphEntity,
                DbDataProviderFactory.createDbDataProvider(
                        DbDataProviderDatas.merge(
                                ImmutableSet
                                        .<DbDataProviderData>builder()
                                        .addAll(getDbDataProviderDatas(transformedGraph.graph().nodes()))
                                        .add(
                                                DbDataProviderData
                                                        .builder()
                                                        .withNodesByGraph(
                                                                Map.of(
                                                                        graphEntity,
                                                                        removeDetachedDbDataProviders(transformedGraph).graph().nodes()))
                                                        .build())
                                        .build())));
    }

    private static Set<DbDataProviderData> getDbDataProviderDatas(final Set<DetachedSearchablePreferenceScreenEntity> detachedSearchablePreferenceScreenEntities) {
        return detachedSearchablePreferenceScreenEntities
                .stream()
                .map(DetachedSearchablePreferenceScreenEntity::dbDataProviderData)
                .collect(Collectors.toSet());
    }

    private static TreeTransformer<SearchablePreferenceScreen, SearchablePreference, DetachedSearchablePreferenceScreenEntity, SearchablePreferenceEntity> createGraphTransformer(final Locale graphId) {
        return new TreeTransformer<>() {

            @Override
            public DetachedSearchablePreferenceScreenEntity transformRootNode(final SearchablePreferenceScreen rootNode) {
                return SearchablePreferenceScreenToSearchablePreferenceScreenEntityConverter.toEntity(
                        rootNode,
                        Optional.empty(),
                        graphId);
            }

            @Override
            public DetachedSearchablePreferenceScreenEntity transformInnerNode(final SearchablePreferenceScreen innerNode,
                                                                               final ContextOfInnerNode<SearchablePreference, DetachedSearchablePreferenceScreenEntity> contextOfInnerNode) {
                return SearchablePreferenceScreenToSearchablePreferenceScreenEntityConverter.toEntity(
                        innerNode,
                        Optional.of(
                                getPreferenceById(
                                        contextOfInnerNode.transformedParentNode(),
                                        contextOfInnerNode.valueOfEdgeFromParentNodeToInnerNode().getId())),
                        graphId);
            }

            @Override
            public SearchablePreferenceEntity transformEdgeValue(final SearchablePreference edgeValue,
                                                                 final DetachedSearchablePreferenceScreenEntity transformedParentNode) {
                return getPreferenceById(transformedParentNode, edgeValue.getId());
            }

            private static SearchablePreferenceEntity getPreferenceById(final DetachedSearchablePreferenceScreenEntity screen,
                                                                        final String id) {
                return SearchablePreferenceEntities
                        .findPreferenceById(
                                screen.getAllPreferencesOfPreferenceHierarchy(),
                                id)
                        .orElseThrow();
            }
        };
    }

    private static Tree<SearchablePreferenceScreenEntity, SearchablePreferenceEntity, ? extends ValueGraph<SearchablePreferenceScreenEntity, SearchablePreferenceEntity>> removeDetachedDbDataProviders(final Tree<DetachedSearchablePreferenceScreenEntity, SearchablePreferenceEntity, ? extends ValueGraph<DetachedSearchablePreferenceScreenEntity, SearchablePreferenceEntity>> graph) {
        return TreeTransformerAlgorithm.transform(
                graph,
                new TreeTransformer<>() {

                    @Override
                    public SearchablePreferenceScreenEntity transformRootNode(final DetachedSearchablePreferenceScreenEntity rootNode) {
                        return rootNode.screen();
                    }

                    @Override
                    public SearchablePreferenceScreenEntity transformInnerNode(final DetachedSearchablePreferenceScreenEntity innerNode,
                                                                               final ContextOfInnerNode<SearchablePreferenceEntity, SearchablePreferenceScreenEntity> contextOfInnerNode) {
                        return innerNode.screen();
                    }

                    @Override
                    public SearchablePreferenceEntity transformEdgeValue(final SearchablePreferenceEntity edgeValue,
                                                                         final SearchablePreferenceScreenEntity transformedParentNode) {
                        return edgeValue;
                    }
                });
    }
}
