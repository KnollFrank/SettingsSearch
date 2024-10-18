package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.PreferenceManager;

import org.jgrapht.Graph;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClassFromPOJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceCaster;
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

            @Override
            public PreferenceScreenWithHostClass transformNode(final PreferenceScreenWithHostClassPOJO node) {
                return PreferenceScreenWithHostClassFromPOJOConverter
                        .convertFromPOJO(node, preferenceManager)
                        .preferenceScreenWithHostClass();
            }

            @Override
            public PreferenceEdge transformEdge(final SearchablePreferencePOJOEdge edge, final PreferenceScreenWithHostClass transformedParentNode) {
                return new PreferenceEdge(
                        getSearchablePreferenceHavingOrigin(
                                SearchablePreferenceCaster.cast(Preferences.getAllChildren(transformedParentNode.preferenceScreen())),
                                Optional.of(edge.preference)));
            }

            private static SearchablePreference getSearchablePreferenceHavingOrigin(
                    final List<SearchablePreference> haystack,
                    final Optional<SearchablePreferencePOJO> origin) {
                return haystack
                        .stream()
                        .filter(searchablePreference -> searchablePreference.getOrigin().equals(origin))
                        .findFirst()
                        .get();
            }
        };
    }
}
