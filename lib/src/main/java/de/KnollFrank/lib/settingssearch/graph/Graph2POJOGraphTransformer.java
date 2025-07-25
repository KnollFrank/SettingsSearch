package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;

import org.jgrapht.Graph;

import java.util.Locale;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.PreferenceFragmentIdProvider;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreen2SearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;

public class Graph2POJOGraphTransformer {

    private final PreferenceScreen2SearchablePreferenceScreenConverter preferenceScreen2SearchablePreferenceScreenConverter;
    private final PreferenceFragmentIdProvider preferenceFragmentIdProvider;

    public Graph2POJOGraphTransformer(final PreferenceScreen2SearchablePreferenceScreenConverter preferenceScreen2SearchablePreferenceScreenConverter,
                                      final PreferenceFragmentIdProvider preferenceFragmentIdProvider) {
        this.preferenceScreen2SearchablePreferenceScreenConverter = preferenceScreen2SearchablePreferenceScreenConverter;
        this.preferenceFragmentIdProvider = preferenceFragmentIdProvider;
    }

    public Graph<SearchablePreferenceScreenWithMap, SearchablePreferenceEdge> transformGraph2POJOGraph(
            final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph,
            final Locale locale) {
        return GraphTransformerAlgorithm.transform(
                preferenceScreenGraph,
                SearchablePreferenceEdge.class,
                createGraphTransformer(locale));
    }

    private GraphTransformer<PreferenceScreenWithHost, PreferenceEdge, SearchablePreferenceScreenWithMap, SearchablePreferenceEdge> createGraphTransformer(
            final Locale locale) {
        final PreferenceFragmentIdProvider preferenceFragmentIdProvider = createPreferenceFragmentUniqueLocalizedIdProvider(locale);
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
            public SearchablePreferenceEdge transformEdge(final PreferenceEdge edge,
                                                          final SearchablePreferenceScreenWithMap transformedParentNode) {
                return new SearchablePreferenceEdge(
                        getTransformedPreference(
                                edge.preference,
                                transformedParentNode));
            }

            private SearchablePreferenceScreenWithMap convert2POJO(
                    final PreferenceScreenWithHost node,
                    final Optional<SearchablePreference> predecessorOfNode) {
                return preferenceScreen2SearchablePreferenceScreenConverter.convertPreferenceScreen(
                        node.preferenceScreen(),
                        node.host(),
                        preferenceFragmentIdProvider.getId(node.host()),
                        predecessorOfNode,
                        locale);
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
        };
    }

    private PreferenceFragmentLocalizedIdProvider createPreferenceFragmentUniqueLocalizedIdProvider(final Locale locale) {
        return new PreferenceFragmentLocalizedIdProvider(
                locale,
                new UniqueIdCheckingPreferenceFragmentIdProvider(preferenceFragmentIdProvider));
    }
}
