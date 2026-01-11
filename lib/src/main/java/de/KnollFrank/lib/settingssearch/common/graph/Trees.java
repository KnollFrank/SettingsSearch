package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ValueGraph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class Trees {

    public static <N, V> TreePath<N, V> getPathFromRootNodeToTarget(final Tree<N, V> tree, final N target) {
        if (!tree.graph().nodes().contains(target)) {
            throw new IllegalArgumentException("Target node " + target + " is not part of the tree.");
        }
        final LinkedList<N> path = new LinkedList<>();
        for (Optional<N> current = Optional.of(target); current.isPresent(); current = tree.parentNodeOf(current.get())) {
            path.addFirst(current.get());
        }
        return new TreePath<>(tree, List.copyOf(path));
    }

    public static <N, V> List<Edge<N, V>> getEdgesOnPath(final TreePath<N, V> path) {
        final List<N> nodes = path.nodes();
        if (nodes.size() <= 1) {
            return List.of();
        }
        final ValueGraph<N, V> graph = path.tree().graph();
        final List<Edge<N, V>> edges = new ArrayList<>();
        for (int i = 0; i < nodes.size() - 1; i++) {
            final N source = nodes.get(i);
            final N target = nodes.get(i + 1);
            final EndpointPair<N> endpointPair = EndpointPair.ordered(source, target);
            final V edgeValue = graph.edgeValueOrDefault(source, target, null);
            edges.add(new Edge<>(endpointPair, edgeValue));
        }
        return List.copyOf(edges);
    }
}
