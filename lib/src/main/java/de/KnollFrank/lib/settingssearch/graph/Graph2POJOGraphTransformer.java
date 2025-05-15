package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;

import org.jgrapht.Graph;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGenerator;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGeneratorFactory;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHost2POJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;

public class Graph2POJOGraphTransformer {

    public static Graph<SearchablePreferenceScreenWithMap, SearchablePreferenceEdge> transformGraph2POJOGraph(
            final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph,
            final Preference2SearchablePreferenceConverter preference2SearchablePreferenceConverter) {
        return GraphTransformerAlgorithm.transform(
                preferenceScreenGraph,
                SearchablePreferenceEdge.class,
                createGraphTransformer(preference2SearchablePreferenceConverter));
    }

    private static GraphTransformer<PreferenceScreenWithHost, PreferenceEdge, SearchablePreferenceScreenWithMap, SearchablePreferenceEdge> createGraphTransformer(
            final Preference2SearchablePreferenceConverter preference2SearchablePreferenceConverter) {
        return new GraphTransformer<>() {

            private final IdGenerator idGenerator4PreferenceScreen = IdGeneratorFactory.createIdGeneratorStartingAt(1);

            @Override
            public SearchablePreferenceScreenWithMap transformRootNode(final PreferenceScreenWithHost rootNode) {
                return convert2POJO(rootNode, Optional.empty(), Optional.empty());
            }

            @Override
            public SearchablePreferenceScreenWithMap transformInnerNode(
                    final PreferenceScreenWithHost innerNode,
                    final ContextOfInnerNode<PreferenceEdge, SearchablePreferenceScreenWithMap> contextOfInnerNode) {
                return convert2POJO(
                        innerNode,
                        Optional.of(getParentId(contextOfInnerNode)),
                        Optional.of(
                                getPredecessorOfNode(
                                        contextOfInnerNode.transformedParentNode(),
                                        contextOfInnerNode.edgeFromParentNode2InnerNode())));
            }

            @Override
            public SearchablePreferenceEdge transformEdge(final PreferenceEdge edge,
                                                          final SearchablePreferenceScreenWithMap transformedParentNode) {
                return new SearchablePreferenceEdge(
                        getTransformedPreference(
                                edge.preference,
                                transformedParentNode));
            }

            private SearchablePreferenceScreenWithMap convert2POJO(
                    final PreferenceScreenWithHost node,
                    final Optional<String> parentId,
                    final Optional<SearchablePreference> predecessorOfNode) {
                return PreferenceScreenWithHost2POJOConverter
                        .convert2POJO(
                                node,
                                // FK-TODO: this String id has to be created by the user of this library, e.g. in node.host() which has to implement the yet to be introduced interface StringIdGenerator?
                                String.valueOf(idGenerator4PreferenceScreen.nextId()),
                                parentId,
                                preference2SearchablePreferenceConverter,
                                predecessorOfNode);
            }

            private static SearchablePreference getPredecessorOfNode(
                    final SearchablePreferenceScreenWithMap parentNode,
                    final PreferenceEdge edgeFromParentNode2Node) {
                return getTransformedPreference(
                        edgeFromParentNode2Node.preference,
                        parentNode);
            }

            private static SearchablePreference getTransformedPreference(
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
