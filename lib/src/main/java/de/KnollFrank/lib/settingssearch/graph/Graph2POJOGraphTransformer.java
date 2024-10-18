package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGenerator;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter.PreferenceScreenWithHostClassPOJOWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;

public class Graph2POJOGraphTransformer {

    public static Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> transformGraph2POJOGraph(
            final Graph<PreferenceScreenWithHostClass, PreferenceEdge> preferenceScreenGraph) {
        return GraphTransformerAlgorithm.transform(
                preferenceScreenGraph,
                SearchablePreferencePOJOEdge.class,
                createGraphTransformer());
    }

    private static GraphTransformer<PreferenceScreenWithHostClass, PreferenceEdge, PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> createGraphTransformer() {
        return new GraphTransformer<>() {

            private final IdGenerator idGenerator4PreferenceScreen = new IdGenerator();
            private final IdGenerator idGenerator4SearchablePreference = new IdGenerator();
            private final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap = HashBiMap.create();

            @Override
            public PreferenceScreenWithHostClassPOJO transformNode(final PreferenceScreenWithHostClass node) {
                final PreferenceScreenWithHostClassPOJOWithMap preferenceScreenWithHostClassPOJOWithMap =
                        PreferenceScreenWithHostClass2POJOConverter
                                .convert2POJO(
                                        node,
                                        idGenerator4PreferenceScreen.nextId(),
                                        idGenerator4SearchablePreference);
                pojoEntityMap.putAll(preferenceScreenWithHostClassPOJOWithMap.pojoEntityMap());
                return preferenceScreenWithHostClassPOJOWithMap.preferenceScreenWithHostClass();
            }

            @Override
            public SearchablePreferencePOJOEdge transformEdge(final PreferenceEdge edge, final PreferenceScreenWithHostClassPOJO transformedParentNode) {
                return new SearchablePreferencePOJOEdge(getSearchablePreferencePOJO(edge.preference));
            }

            private SearchablePreferencePOJO getSearchablePreferencePOJO(final Preference preference) {
                return pojoEntityMap.inverse().get(preference);
            }
        };
    }
}
