package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.common.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.IGraphTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;

public class Graph2POJOGraphTransformer {

    public static Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> transformGraph2POJOGraph(
            final Graph<PreferenceScreenWithHostClass, PreferenceEdge> preferenceScreenGraph) {
        return GraphTransformer.transform(
                preferenceScreenGraph,
                SearchablePreferencePOJOEdge.class,
                createGraphTransformer());
    }

    private static IGraphTransformer<PreferenceScreenWithHostClass, PreferenceEdge, PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> createGraphTransformer() {
        return new IGraphTransformer<>() {

            @Override
            public PreferenceScreenWithHostClassPOJO transformNode(final PreferenceScreenWithHostClass node) {
                return PreferenceScreenWithHostClass2POJOConverter.convert2POJO(node);
            }

            @Override
            public SearchablePreferencePOJOEdge transformEdge(final PreferenceEdge edge, final PreferenceScreenWithHostClassPOJO transformedParentNode) {
                return null; // FK-FIXME: new SearchablePreferencePOJOEdge(edge.preference);
            }
        };
    }
}
