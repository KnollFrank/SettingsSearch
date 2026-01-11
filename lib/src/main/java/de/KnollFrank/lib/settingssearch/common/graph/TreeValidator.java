package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.Iterables;
import com.google.common.graph.Traverser;
import com.google.common.graph.ValueGraph;

import java.util.Locale;
import java.util.Optional;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
class TreeValidator {

    public static <Node> TreeValidatorResult validateIsTree(final ValueGraph<Node, ?> graph) {
        if (!graph.isDirected()) {
            return TreeValidatorResult.invalid("A Tree must be a directed graph.");
        }
        if (ValueGraphs.isEmpty(graph)) {
            return TreeValidatorResult.invalid("The graph must not be empty.");
        }
        {
            final int numberOfNodes = graph.nodes().size();
            final int numberOfEdges = graph.edges().size();
            if (numberOfEdges != numberOfNodes - 1) {
                return TreeValidatorResult.invalid(
                        String.format(
                                Locale.ROOT,
                                "A tree with %d nodes must have exactly %d edges, but found %d.",
                                numberOfNodes,
                                numberOfNodes - 1,
                                numberOfEdges));
            }
        }
        {
            final int rootCount = ValueGraphs.getRootNodes(graph).size();
            if (rootCount != 1) {
                return TreeValidatorResult.invalid("A Tree must have exactly one root node, but found: " + rootCount);
            }
        }
        {
            final Optional<Node> nodeWithMultiplePredecessors = findNodeWithMultiplePredecessors(graph);
            if (nodeWithMultiplePredecessors.isPresent()) {
                final Node _nodeWithMultiplePredecessors = nodeWithMultiplePredecessors.orElseThrow();
                return TreeValidatorResult.invalid(
                        String.format(
                                "Node %s has multiple predecessors %s.",
                                _nodeWithMultiplePredecessors,
                                graph.predecessors(_nodeWithMultiplePredecessors)));
            }
        }
        if (!allNodesReachableFromRootNode(graph)) {
            return TreeValidatorResult.invalid("The graph is not connected; not all nodes are reachable from the root node.");
        }
        return TreeValidatorResult.valid();
    }

    private static <Node> Optional<Node> findNodeWithMultiplePredecessors(final ValueGraph<Node, ?> graph) {
        return graph
                .nodes()
                .stream()
                .filter(node -> graph.inDegree(node) > 1)
                .findAny();
    }

    private static <Node> boolean allNodesReachableFromRootNode(final ValueGraph<Node, ?> graph) {
        return getReachableCountFromRootNode(graph) == graph.nodes().size();
    }

    private static <Node> int getReachableCountFromRootNode(final ValueGraph<Node, ?> graph) {
        return Iterables.size(
                Traverser
                        .forGraph(graph)
                        .depthFirstPreOrder(ValueGraphs.getRootNode(graph).orElseThrow()));
    }
}
