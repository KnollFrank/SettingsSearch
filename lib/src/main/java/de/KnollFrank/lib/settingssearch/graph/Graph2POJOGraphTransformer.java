package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;

import org.jgrapht.Graph;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGenerator;
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

            private final IdGenerator idGenerator4PreferenceScreen = new IdGenerator();

            @Override
            public SearchablePreferenceScreenWithMap transformNode(
                    final PreferenceScreenWithHost node,
                    final Optional<NodeContext<PreferenceEdge, SearchablePreferenceScreenWithMap>> nodeContext) {
                return PreferenceScreenWithHost2POJOConverter
                        .convert2POJO(
                                node,
                                idGenerator4PreferenceScreen.nextId(),
                                preference2SearchablePreferenceConverter,
                                // FK-TODO: refactor
                                getPredecessorOfNode(nodeContext.map(NodeContext::transformedParentNode), nodeContext.map(NodeContext::edgeFromParentNode2Node)));
            }

            @Override
            public SearchablePreferenceEdge transformEdge(final PreferenceEdge edge,
                                                          final SearchablePreferenceScreenWithMap transformedParentNode) {
                return new SearchablePreferenceEdge(
                        getTransformedPreference(
                                edge.preference,
                                transformedParentNode));
            }

            private static Optional<SearchablePreference> getPredecessorOfNode(
                    final Optional<SearchablePreferenceScreenWithMap> parentNode,
                    final Optional<PreferenceEdge> edgeFromParentNode2Node) {
                return parentNode.map(
                        _parentNode ->
                                getTransformedPreference(
                                        edgeFromParentNode2Node.orElseThrow().preference,
                                        _parentNode));
            }

            private static SearchablePreference getTransformedPreference(
                    final Preference preference,
                    final SearchablePreferenceScreenWithMap transformedParentNode) {
                return transformedParentNode
                        .pojoEntityMap()
                        .inverse()
                        .get(preference);
            }
        };
    }
}
