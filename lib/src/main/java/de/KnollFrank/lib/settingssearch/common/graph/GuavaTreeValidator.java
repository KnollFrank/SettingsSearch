package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.ValueGraph;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
class GuavaTreeValidator {

    public static <V, W> TreeValidatorResult validateIsTree(final ValueGraph<V, W> graph) {
        // A tree must be a directed graph.
        if (!graph.isDirected()) {
            return TreeValidatorResult.invalid("A Tree must be a directed graph.");
        }
        if (isEmpty(graph)) {
            return TreeValidatorResult.invalid("The graph must not be empty.");
        }
        int rootCount = 0;
        for (final V node : graph.nodes()) {
            final int inDegree = graph.inDegree(node);

            // Each node must have an in-degree of at most 1.
            if (inDegree > 1) {
                return TreeValidatorResult.invalid(
                        String.format(
                                "Node '%s' has more than one parent (in-degree: %d). A tree is not allowed to have nodes with in-degree > 1.",
                                node,
                                inDegree));
            }

            // Count nodes with in-degree 0 (potential roots).
            if (inDegree == 0) {
                rootCount++;
            }
        }

        // There must be exactly one root.
        if (rootCount != 1) {
            return TreeValidatorResult.invalid(
                    "A tree must have exactly one root (a node with in-degree 0), but found: " + rootCount);
        }

        return TreeValidatorResult.valid();
    }

    private static <V, W> boolean isEmpty(final ValueGraph<V, W> graph) {
        return graph.nodes().isEmpty();
    }
}
