package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;

import com.google.common.collect.ImmutableList;

import org.jgrapht.Graph;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverter;
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

            private int id = 1;

            @Override
            public PreferenceScreenWithHostClassPOJO transformNode(final PreferenceScreenWithHostClass node) {
                return PreferenceScreenWithHostClass2POJOConverter.convert2POJO(node, id++);
            }

            @Override
            public SearchablePreferencePOJOEdge transformEdge(final PreferenceEdge edge, final PreferenceScreenWithHostClassPOJO transformedParentNode) {
                return new SearchablePreferencePOJOEdge(
                        getSearchablePreferencePOJOHavingOrigin(
                                getAllPreferences(transformedParentNode.preferenceScreen().children()),
                                Optional.of(edge.preference)));
            }

            private static SearchablePreferencePOJO getSearchablePreferencePOJOHavingOrigin(
                    final List<SearchablePreferencePOJO> haystack,
                    final Optional<Preference> origin) {
                return haystack
                        .stream()
                        .filter(searchablePreferencePOJO -> searchablePreferencePOJO.origin().equals(origin))
                        .findFirst()
                        .get();
            }

            private static List<SearchablePreferencePOJO> getAllPreferences(final SearchablePreferencePOJO searchablePreference) {
                return ImmutableList.<SearchablePreferencePOJO>builder()
                        .add(searchablePreference)
                        .addAll(getAllPreferences(searchablePreference.children()))
                        .build();
            }

            private static List<SearchablePreferencePOJO> getAllPreferences(final List<SearchablePreferencePOJO> searchablePreferencePOJOs) {
                return searchablePreferencePOJOs
                        .stream()
                        .flatMap(searchablePreferencePOJO -> getAllPreferences(searchablePreferencePOJO).stream())
                        .collect(Collectors.toList());
            }
        };
    }
}
