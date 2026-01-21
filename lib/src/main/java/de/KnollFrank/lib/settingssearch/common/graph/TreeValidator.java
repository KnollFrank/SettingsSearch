package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.Sets;
import com.google.common.graph.ValueGraph;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
class TreeValidator {

    private TreeValidator() {
    }

    public static <Node> TreeValidatorResult validateIsTree(final ValueGraph<Node, ?> graph) {
        if (!graph.isDirected()) {
            return TreeValidatorResult.invalid("A Tree must be a directed graph.");
        }
        if (Graphs.isEmpty(graph)) {
            return TreeValidatorResult.invalid("The graph must not be empty.");
        }
        {
            final int rootCount = Graphs.getRootNodes(graph).size();
            if (rootCount != 1) {
                return TreeValidatorResult.invalid(
                        String.format(
                                Locale.ROOT,
                                "A Tree must have exactly one root node, but found %d: %s",
                                rootCount,
                                Graphs.getRootNodes(graph)));
            }
        }
        {
            final Optional<Node> nodeWithMultiplePredecessors = findNodeWithMultiplePredecessors(graph);
            if (nodeWithMultiplePredecessors.isPresent()) {
                final Node _nodeWithMultiplePredecessors = nodeWithMultiplePredecessors.orElseThrow();
                return TreeValidatorResult.invalid(
                        String.format(
                                "Node '%s' is not a valid tree node because it has multiple predecessors: %s.",
                                _nodeWithMultiplePredecessors,
                                graph.predecessors(_nodeWithMultiplePredecessors)));
            }
        }
        {
            final Set<Node> unreachableNodes = getUnreachableNodesFromRootNode(graph);
            if (!unreachableNodes.isEmpty()) {
                return TreeValidatorResult.invalid(
                        String.format(
                                "The graph is not connected. The following nodes are not reachable from the root node: %s",
                                unreachableNodes));
            }
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
        return TreeValidatorResult.valid();
    }

    private static <Node> Optional<Node> findNodeWithMultiplePredecessors(final ValueGraph<Node, ?> graph) {
        return graph
                .nodes()
                .stream()
                .filter(node -> graph.inDegree(node) > 1)
                .findAny();
    }

    private static <Node> Set<Node> getUnreachableNodesFromRootNode(final ValueGraph<Node, ?> graph) {
        return Sets.difference(graph.nodes(), getReachableNodesFromRootNode(graph));
    }

    private static <Node> Set<Node> getReachableNodesFromRootNode(final ValueGraph<Node, ?> graph) {
        return com.google.common.graph.Graphs.reachableNodes(
                graph.asGraph(),
                Graphs.getRootNode(graph).orElseThrow());
    }
}
