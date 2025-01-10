package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGenerator;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter.PreferenceScreenWithHostClassWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;

public class Graph2POJOGraphTransformer {

    public static Graph<PreferenceScreenWithHostClassWithMap, SearchablePreferenceEdge> transformGraph2POJOGraph(
            final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph,
            final Preference2SearchablePreferenceConverter preference2SearchablePreferenceConverter) {
        return GraphTransformerAlgorithm.transform(
                preferenceScreenGraph,
                SearchablePreferenceEdge.class,
                createGraphTransformer(preference2SearchablePreferenceConverter));
    }

    private static GraphTransformer<PreferenceScreenWithHost, PreferenceEdge, PreferenceScreenWithHostClassWithMap, SearchablePreferenceEdge> createGraphTransformer(
            final Preference2SearchablePreferenceConverter preference2SearchablePreferenceConverter) {
        return new GraphTransformer<>() {

            private final IdGenerator idGenerator4PreferenceScreen = new IdGenerator();

            @Override
            public PreferenceScreenWithHostClassWithMap transformNode(final PreferenceScreenWithHost node) {
                return PreferenceScreenWithHostClass2POJOConverter
                        .convert2POJO(
                                node,
                                idGenerator4PreferenceScreen.nextId(),
                                preference2SearchablePreferenceConverter);
            }

            @Override
            public SearchablePreferenceEdge transformEdge(final PreferenceEdge edge,
                                                          final PreferenceScreenWithHostClassWithMap transformedParentNode) {
                return new SearchablePreferenceEdge(
                        getTransformedPreference(
                                edge.preference,
                                transformedParentNode));
            }

            private static SearchablePreference getTransformedPreference(
                    final Preference preference,
                    final PreferenceScreenWithHostClassWithMap transformedParentNode) {
                return transformedParentNode
                        .pojoEntityMap()
                        .inverse()
                        .get(preference);
            }
        };
    }
}
