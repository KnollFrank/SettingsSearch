package de.KnollFrank.lib.settingssearch.common.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.jgrapht.Graph;

import java.util.Set;
import java.util.stream.Collectors;

class GraphEquality {

    public static void assertActualEqualsExpected(final Graph<StringVertex, StringEdge> actual,
                                                  final Graph<StringVertex, StringEdge> expected) {
        assertActualEqualsExpected(actual.vertexSet(), expected.vertexSet());
        assertActualEdgesEqualsExpectedEdges(actual, expected);
    }

    private static void assertActualEqualsExpected(final Set<StringVertex> nodesActual, final Set<StringVertex> nodesExpected) {
        assertThat(
                "Vertex sets should be equal. Expected: [" + nodes2String(nodesExpected) + "], Actual: [" + nodes2String(nodesActual) + "]",
                nodesActual,
                is(equalTo(nodesExpected)));
    }

    private static void assertActualEdgesEqualsExpectedEdges(final Graph<StringVertex, StringEdge> actual, final Graph<StringVertex, StringEdge> expected) {
        final Set<String> expectedEdgesRepr = edgesAsStrings(expected);
        final Set<String> actualEdgesRepr = edgesAsStrings(actual);
        assertThat(
                "Edge representations should be equal. Expected: " + expectedEdgesRepr + ", Actual: " + actualEdgesRepr,
                actualEdgesRepr,
                is(equalTo(expectedEdgesRepr)));
    }

    private static String nodes2String(final Set<StringVertex> nodes) {
        return nodes
                .stream()
                .map(StringVertex::getLabel)
                .sorted()
                .collect(Collectors.joining(", "));
    }

    private static Set<String> edgesAsStrings(final Graph<StringVertex, StringEdge> graph) {
        return graph
                .edgeSet()
                .stream()
                .map(edge -> graph.getEdgeSource(edge).getLabel() + "->" + graph.getEdgeTarget(edge).getLabel() + ":" + edge.getLabel())
                .collect(Collectors.toSet());
    }
}
