package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGenerator;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter.PreferenceScreenWithHostClassPOJOWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;

public class Graph2POJOGraphTransformer {

    public static Graph<PreferenceScreenWithHostClassPOJOWithMap, SearchablePreferencePOJOEdge> transformGraph2POJOGraph(
            final Graph<PreferenceScreenWithHostClass, PreferenceEdge> preferenceScreenGraph) {
        return GraphTransformerAlgorithm.transform(
                preferenceScreenGraph,
                SearchablePreferencePOJOEdge.class,
                createGraphTransformer());
    }

    private static GraphTransformer<PreferenceScreenWithHostClass, PreferenceEdge, PreferenceScreenWithHostClassPOJOWithMap, SearchablePreferencePOJOEdge> createGraphTransformer() {
        return new GraphTransformer<>() {

            private final IdGenerator idGenerator4PreferenceScreen = new IdGenerator();
            private final IdGenerator idGenerator4SearchablePreference = new IdGenerator();

            @Override
            public PreferenceScreenWithHostClassPOJOWithMap transformNode(final PreferenceScreenWithHostClass node) {
                return PreferenceScreenWithHostClass2POJOConverter
                        .convert2POJO(
                                node,
                                idGenerator4PreferenceScreen.nextId(),
                                idGenerator4SearchablePreference);
            }

            @Override
            public SearchablePreferencePOJOEdge transformEdge(final PreferenceEdge edge,
                                                              final PreferenceScreenWithHostClassPOJOWithMap transformedParentNode) {
                return new SearchablePreferencePOJOEdge(
                        getTransformedPreference(
                                edge.preference,
                                transformedParentNode));
            }

            private static SearchablePreferencePOJO getTransformedPreference(
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
