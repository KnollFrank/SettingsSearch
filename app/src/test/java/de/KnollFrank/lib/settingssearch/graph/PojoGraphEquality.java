package de.KnollFrank.lib.settingssearch.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.jgrapht.Graph;

import java.util.Comparator;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

class PojoGraphEquality {

    public static void assertActualEqualsExpected(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> actual,
                                                  final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> expected) {
        assertActualEqualsExpected(actual.vertexSet(), expected.vertexSet());
        assertActualEdgesEqualsExpectedEdges(actual, expected);
    }

    private static void assertActualEqualsExpected(final Set<SearchablePreferenceScreen> nodesActual, final Set<SearchablePreferenceScreen> nodesExpected) {
        assertThat(
                "Vertex sets should be equal. Expected: [" + nodes2String(nodesExpected) + "], Actual: [" + nodes2String(nodesActual) + "]",
                nodes2String(nodesActual),
                is(equalTo(nodes2String(nodesExpected))));
    }

    private static void assertActualEdgesEqualsExpectedEdges(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> actual, final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> expected) {
        final Set<String> expectedEdgesRepr = edgesAsStrings(expected);
        final Set<String> actualEdgesRepr = edgesAsStrings(actual);
        assertThat(
                "Edge representations should be equal. Expected: " + expectedEdgesRepr + ", Actual: " + actualEdgesRepr,
                actualEdgesRepr,
                is(equalTo(expectedEdgesRepr)));
    }

    private static String nodes2String(final Set<SearchablePreferenceScreen> nodes) {
        return nodes
                .stream()
                .sorted(Comparator.comparing(SearchablePreferenceScreen::id))
                .map(PojoGraphEquality::toString)
                .collect(Collectors.joining(", "));
    }

    private static Set<String> edgesAsStrings(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph) {
        return graph
                .edgeSet()
                .stream()
                .map(edge -> graph.getEdgeSource(edge).id() + "->" + graph.getEdgeTarget(edge).id() + ":" + edge.preference)
                .collect(Collectors.toSet());
    }

    private static String toString(final SearchablePreferenceScreen searchablePreferenceScreen) {
        return new StringJoiner(", ", SearchablePreferenceScreen.class.getSimpleName() + "[", "]")
                .add("id='" + searchablePreferenceScreen.id() + "'")
                .add("host=" + searchablePreferenceScreen.host())
                .add("title=" + searchablePreferenceScreen.title())
                .add("summary=" + searchablePreferenceScreen.summary())
                .add("allPreferences=" + toString(searchablePreferenceScreen.allPreferences()))
                .toString();
    }

    private static String toString(final Set<SearchablePreference> searchablePreferences) {
        return searchablePreferences
                .stream()
                .sorted(Comparator.comparing(SearchablePreference::getId))
                .map(SearchablePreference::toString)
                .collect(Collectors.joining(", "));
    }
}
