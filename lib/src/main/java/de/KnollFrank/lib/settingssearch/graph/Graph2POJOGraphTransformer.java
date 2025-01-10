package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGenerator;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferencePOJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter.PreferenceScreenWithHostClassPOJOWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;

public class Graph2POJOGraphTransformer {

    public static Graph<PreferenceScreenWithHostClassPOJOWithMap, SearchablePreferencePOJOEdge> transformGraph2POJOGraph(
            final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph,
            final Preference2SearchablePreferencePOJOConverter preference2SearchablePreferencePOJOConverter) {
        return GraphTransformerAlgorithm.transform(
                preferenceScreenGraph,
                SearchablePreferencePOJOEdge.class,
                createGraphTransformer(preference2SearchablePreferencePOJOConverter));
    }

    private static GraphTransformer<PreferenceScreenWithHost, PreferenceEdge, PreferenceScreenWithHostClassPOJOWithMap, SearchablePreferencePOJOEdge> createGraphTransformer(
            final Preference2SearchablePreferencePOJOConverter preference2SearchablePreferencePOJOConverter) {
        return new GraphTransformer<>() {

            private final IdGenerator idGenerator4PreferenceScreen = new IdGenerator();

            @Override
            public PreferenceScreenWithHostClassPOJOWithMap transformNode(final PreferenceScreenWithHost node) {
                return PreferenceScreenWithHostClass2POJOConverter
                        .convert2POJO(
                                node,
                                idGenerator4PreferenceScreen.nextId(),
                                preference2SearchablePreferencePOJOConverter);
            }

            @Override
            public SearchablePreferencePOJOEdge transformEdge(final PreferenceEdge edge,
                                                              final PreferenceScreenWithHostClassPOJOWithMap transformedParentNode) {
                return new SearchablePreferencePOJOEdge(
                        getTransformedPreference(
                                edge.preference,
                                transformedParentNode));
            }

            private static SearchablePreference getTransformedPreference(
                    final Preference preference,
                    final PreferenceScreenWithHostClassPOJOWithMap transformedParentNode) {
                return transformedParentNode
                        .pojoEntityMap()
                        .inverse()
                        .get(preference);
            }
        };
    }
}
