package de.KnollFrank.lib.settingssearch.graph;

import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClassFromPOJOConverter.PreferenceScreenWithHostClassWithMap;

import androidx.preference.PreferenceManager;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClassFromPOJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;

public class POJOGraph2GraphTransformer {

    public static Graph<PreferenceScreenWithHostClassWithMap, PreferenceEdge> transformPOJOGraph2Graph(
            final Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> pojoGraph,
            final PreferenceManager preferenceManager) {
        return GraphTransformerAlgorithm.transform(
                pojoGraph,
                PreferenceEdge.class,
                createGraphTransformer(preferenceManager));
    }

    private static GraphTransformer<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge, PreferenceScreenWithHostClassWithMap, PreferenceEdge> createGraphTransformer(
            final PreferenceManager preferenceManager) {
        return new GraphTransformer<>() {

            @Override
            public PreferenceScreenWithHostClassWithMap transformNode(final PreferenceScreenWithHostClassPOJO node) {
                return PreferenceScreenWithHostClassFromPOJOConverter.convertFromPOJO(node, preferenceManager);
            }

            @Override
            public PreferenceEdge transformEdge(final SearchablePreferencePOJOEdge edge, final PreferenceScreenWithHostClassWithMap transformedParentNode) {
                return new PreferenceEdge(
                        getTransformedPreference(
                                edge.preference,
                                transformedParentNode));
            }

            private static SearchablePreference getTransformedPreference(
                    final SearchablePreferencePOJO preference,
                    final PreferenceScreenWithHostClassWithMap transformedParentNode) {
                return transformedParentNode.pojoEntityMap().get(preference);
            }
        };
    }
}
