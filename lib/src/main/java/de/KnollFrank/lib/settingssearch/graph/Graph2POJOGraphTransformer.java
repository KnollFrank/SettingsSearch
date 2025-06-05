package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;

import org.jgrapht.Graph;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.PreferenceFragmentIdProvider;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreen2SearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;

public class Graph2POJOGraphTransformer {

    private final PreferenceScreen2SearchablePreferenceScreenConverter preferenceScreen2SearchablePreferenceScreenConverter;
    private final PreferenceFragmentIdProvider preferenceFragmentIdProvider;

    public Graph2POJOGraphTransformer(final PreferenceScreen2SearchablePreferenceScreenConverter preferenceScreen2SearchablePreferenceScreenConverter,
                                      final PreferenceFragmentIdProvider preferenceFragmentIdProvider) {
        this.preferenceScreen2SearchablePreferenceScreenConverter = preferenceScreen2SearchablePreferenceScreenConverter;
        this.preferenceFragmentIdProvider = preferenceFragmentIdProvider;
    }

    public Graph<SearchablePreferenceScreenWithMap, SearchablePreferenceEntityEdge> transformGraph2POJOGraph(
            final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        return GraphTransformerAlgorithm.transform(
                preferenceScreenGraph,
                SearchablePreferenceEntityEdge.class,
                createGraphTransformer());
    }

    private GraphTransformer<PreferenceScreenWithHost, PreferenceEdge, SearchablePreferenceScreenWithMap, SearchablePreferenceEntityEdge> createGraphTransformer() {
        return new GraphTransformer<>() {

            @Override
            public SearchablePreferenceScreenWithMap transformRootNode(final PreferenceScreenWithHost rootNode) {
                return convert2POJO(rootNode, Optional.empty());
            }

            @Override
            public SearchablePreferenceScreenWithMap transformInnerNode(
                    final PreferenceScreenWithHost innerNode,
                    final ContextOfInnerNode<PreferenceEdge, SearchablePreferenceScreenWithMap> contextOfInnerNode) {
                return convert2POJO(
                        innerNode,
                        Optional.of(
                                getPredecessorOfNode(
                                        contextOfInnerNode.transformedParentNode(),
                                        contextOfInnerNode.edgeFromParentNode2InnerNode())));
            }

            @Override
            public SearchablePreferenceEntityEdge transformEdge(final PreferenceEdge edge,
                                                                final SearchablePreferenceScreenWithMap transformedParentNode) {
                return new SearchablePreferenceEntityEdge(
                        getTransformedPreference(
                                edge.preference,
                                transformedParentNode));
            }

            private SearchablePreferenceScreenWithMap convert2POJO(
                    final PreferenceScreenWithHost node,
                    final Optional<SearchablePreferenceEntity> predecessorOfNode) {
                return preferenceScreen2SearchablePreferenceScreenConverter.convertPreferenceScreen(
                        node.preferenceScreen(),
                        node.host(),
                        preferenceFragmentIdProvider.getId(node.host()),
                        predecessorOfNode);
            }

            private static SearchablePreferenceEntity getPredecessorOfNode(
                    final SearchablePreferenceScreenWithMap parentNode,
                    final PreferenceEdge edgeFromParentNode2Node) {
                return getTransformedPreference(
                        edgeFromParentNode2Node.preference,
                        parentNode);
            }

            private static SearchablePreferenceEntity getTransformedPreference(
                    final Preference preference,
                    final SearchablePreferenceScreenWithMap transformedParentNode) {
                return transformedParentNode
                        .pojoEntityMap()
                        .inverse()
                        .get(preference);
            }

            private static String getParentId(final ContextOfInnerNode<PreferenceEdge, SearchablePreferenceScreenWithMap> contextOfInnerNode) {
                return contextOfInnerNode.transformedParentNode().searchablePreferenceScreen().getId();
            }
        };
    }
}
