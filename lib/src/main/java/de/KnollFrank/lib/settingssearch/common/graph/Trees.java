package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ValueGraph;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Lists;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class Trees {

    private Trees() {
    }

    public static <N, V, G extends ValueGraph<N, V>> TreePath<N, V, G> getPathFromRootNodeToTarget(final Tree<N, V, G> tree, final N target) {
        if (!tree.graph().nodes().contains(target)) {
            throw new IllegalArgumentException("Target node " + target + " is not part of the tree.");
        }
        final LinkedList<N> path = new LinkedList<>();
        for (Optional<N> current = Optional.of(target); current.isPresent(); current = tree.parentNodeOf(current.get())) {
            path.addFirst(current.get());
        }
        return new TreePath<>(tree, List.copyOf(path));
    }

    public static <N, V> List<Edge<N, V>> getEdgesOnPath(final TreePath<N, V, ? extends ValueGraph<N, V>> path) {
        return Lists
                .getConsecutivePairs(path.nodes())
                .stream()
                .map(consecutiveNodePair -> EndpointPair.ordered(consecutiveNodePair.first, consecutiveNodePair.second))
                .map(edge -> Edge.of(edge, path.tree().graph()))
                .toList();
    }
}
