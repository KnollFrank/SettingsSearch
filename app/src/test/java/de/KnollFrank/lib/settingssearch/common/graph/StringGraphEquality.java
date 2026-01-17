package de.KnollFrank.lib.settingssearch.common.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.jgrapht.Graph;

import java.util.Set;
import java.util.stream.Collectors;

class StringGraphEquality {

    public static void assertActualEqualsExpected(final Graph<StringNode, StringEdge> actual,
                                                  final Graph<StringNode, StringEdge> expected) {
        assertActualEqualsExpected(actual.vertexSet(), expected.vertexSet());
        assertActualEdgesEqualsExpectedEdges(actual, expected);
    }

    private static void assertActualEqualsExpected(final Set<StringNode> nodesActual, final Set<StringNode> nodesExpected) {
        assertThat(
                "Vertex sets should be equal. Expected: [" + nodes2String(nodesExpected) + "], Actual: [" + nodes2String(nodesActual) + "]",
                nodesActual,
                is(equalTo(nodesExpected)));
    }

    private static void assertActualEdgesEqualsExpectedEdges(final Graph<StringNode, StringEdge> actual, final Graph<StringNode, StringEdge> expected) {
        final Set<String> expectedEdgesRepr = edgesAsStrings(expected);
        final Set<String> actualEdgesRepr = edgesAsStrings(actual);
        assertThat(
                "Edge representations should be equal. Expected: " + expectedEdgesRepr + ", Actual: " + actualEdgesRepr,
                actualEdgesRepr,
                is(equalTo(expectedEdgesRepr)));
    }

    private static String nodes2String(final Set<StringNode> nodes) {
        return nodes
                .stream()
                .map(StringNode::label)
                .sorted()
                .collect(Collectors.joining(", "));
    }

    private static Set<String> edgesAsStrings(final Graph<StringNode, StringEdge> graph) {
        return graph
                .edgeSet()
                .stream()
                .map(edge -> graph.getEdgeSource(edge).label() + "->" + graph.getEdgeTarget(edge).label() + ":" + edge.getLabel())
                .collect(Collectors.toSet());
    }
}
