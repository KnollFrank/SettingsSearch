package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.PreferenceManager;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.common.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.IGraphTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClassFromPOJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;

public class POJOGraph2GraphTransformer {

    public static Graph<PreferenceScreenWithHostClass, PreferenceEdge> transformPOJOGraph2Graph(
            final Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> pojoGraph,
            final PreferenceManager preferenceManager) {
        return GraphTransformer.transform(
                pojoGraph,
                PreferenceEdge.class,
                createGraphTransformer(preferenceManager));
    }

    private static IGraphTransformer<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge, PreferenceScreenWithHostClass, PreferenceEdge> createGraphTransformer(
            final PreferenceManager preferenceManager) {
        return new IGraphTransformer<>() {

            @Override
            public PreferenceScreenWithHostClass transformNode(final PreferenceScreenWithHostClassPOJO node) {
                return PreferenceScreenWithHostClassFromPOJOConverter.convertFromPOJO(node, preferenceManager);
            }

            @Override
            public PreferenceEdge transformEdge(final SearchablePreferencePOJOEdge edge, final PreferenceScreenWithHostClass transformedParentNode) {
                return null; // FK-FIXME: new SearchablePreferencePOJOEdge(edge.preference);
            }
        };
    }
}
