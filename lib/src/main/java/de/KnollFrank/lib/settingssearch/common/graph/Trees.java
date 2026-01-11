package de.KnollFrank.lib.settingssearch.common.graph;

import androidx.core.util.Pair;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ValueGraph;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

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
        final ValueGraph<N, V> graph = path.tree().graph();
        return getConsecutivePairs(nodes)
                .stream()
                .map(consecutiveNodePair -> EndpointPair.ordered(consecutiveNodePair.first, consecutiveNodePair.second))
                .map(endpointPair -> new Edge<>(endpointPair, graph.edgeValueOrDefault(endpointPair, null)))
                .toList();
    }

    private static <N> List<Pair<N, N>> getConsecutivePairs(final List<N> elements) {
        return IntStream
                .range(0, elements.size() - 1)
                .mapToObj(index -> Pair.create(index, index + 1))
                .map(consecutiveIndexPair -> Pair.create(elements.get(consecutiveIndexPair.first), elements.get(consecutiveIndexPair.second)))
                .toList();
    }
}
