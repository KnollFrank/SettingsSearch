package de.KnollFrank.lib.settingssearch.graph;

import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClassFromPOJOConverter.PreferenceScreenWithHostClassWithMap;

import androidx.preference.PreferenceManager;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClassFromPOJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;

public class POJOGraph2GraphTransformer {

    public static Graph<PreferenceScreenWithHostClass, PreferenceEdge> transformPOJOGraph2Graph(
            final Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> pojoGraph,
            final PreferenceManager preferenceManager) {
        return GraphTransformerAlgorithm.transform(
                pojoGraph,
                PreferenceEdge.class,
                createGraphTransformer(preferenceManager));
    }

    private static GraphTransformer<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge, PreferenceScreenWithHostClass, PreferenceEdge> createGraphTransformer(
            final PreferenceManager preferenceManager) {
        return new GraphTransformer<>() {

            private final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap = HashBiMap.create();

            @Override
            public PreferenceScreenWithHostClass transformNode(final PreferenceScreenWithHostClassPOJO node) {
                final PreferenceScreenWithHostClassWithMap preferenceScreenWithHostClassWithMap =
                        PreferenceScreenWithHostClassFromPOJOConverter
                                .convertFromPOJO(node, preferenceManager);
                pojoEntityMap.putAll(preferenceScreenWithHostClassWithMap.pojoEntityMap());
                return preferenceScreenWithHostClassWithMap.preferenceScreenWithHostClass();
            }

            @Override
            public PreferenceEdge transformEdge(final SearchablePreferencePOJOEdge edge, final PreferenceScreenWithHostClass transformedParentNode) {
                return new PreferenceEdge(pojoEntityMap.get(edge.preference));
            }
        };
    }
}
