package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ValueGraph;

import java.util.Set;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
class GuavaTreeValidator {

    public static <Node, Value> TreeValidatorResult validateIsTree(final ValueGraph<Node, Value> graph) {
        // A tree must be a directed graph.
        if (!graph.isDirected()) {
            return TreeValidatorResult.invalid("A Tree must be a directed graph.");
        }
        if (isEmpty(graph)) {
            return TreeValidatorResult.invalid("The graph must not be empty.");
        }
        {
            final Set<Node> rootNodes = GuavaGraphs.getRootNodes(graph);
            if (rootNodes.size() != 1) {
                return TreeValidatorResult.invalid(
                        String.format(
                                "A Tree must have exactly one root node, but found %d: %s",
                                rootNodes.size(),
                                rootNodes));
            }
        }
        return TreeValidatorResult.valid();
    }

    private static <Node, Value> boolean isEmpty(final ValueGraph<Node, Value> graph) {
        return graph.nodes().isEmpty();
    }
}
