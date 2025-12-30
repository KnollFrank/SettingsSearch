package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;

import org.jgrapht.Graph;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.PreferenceFragmentIdProvider;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenToSearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;

public class GraphToPojoGraphTransformer {

    private final PreferenceScreenToSearchablePreferenceScreenConverter preferenceScreenToSearchablePreferenceScreenConverter;
    private final PreferenceFragmentIdProvider preferenceFragmentIdProvider;

    public GraphToPojoGraphTransformer(final PreferenceScreenToSearchablePreferenceScreenConverter preferenceScreenToSearchablePreferenceScreenConverter,
                                       final PreferenceFragmentIdProvider preferenceFragmentIdProvider) {
        this.preferenceScreenToSearchablePreferenceScreenConverter = preferenceScreenToSearchablePreferenceScreenConverter;
        this.preferenceFragmentIdProvider = preferenceFragmentIdProvider;
    }

    public Graph<SearchablePreferenceScreenWithMap, SearchablePreferenceEdge> transformGraphToPojoGraph(
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
                return convertToPojo(rootNode);
            }

            @Override
            public SearchablePreferenceScreenWithMap transformInnerNode(
                    final PreferenceScreenWithHost innerNode,
                    final ContextOfInnerNode<PreferenceEdge, SearchablePreferenceScreenWithMap> contextOfInnerNode) {
                return convertToPojo(innerNode);
            }

            @Override
            public SearchablePreferenceEdge transformEdge(final PreferenceEdge edge,
                                                          final SearchablePreferenceScreenWithMap transformedParentNode) {
                return new SearchablePreferenceEdge(
                        getTransformedPreference(
                                edge.preference,
                                transformedParentNode));
            }

            private SearchablePreferenceScreenWithMap convertToPojo(final PreferenceScreenWithHost node) {
                return preferenceScreenToSearchablePreferenceScreenConverter.convertPreferenceScreen(
                        node.preferenceScreen(),
                        node.host(),
                        preferenceFragmentIdProvider.getId(node.host()),
                        locale);
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
