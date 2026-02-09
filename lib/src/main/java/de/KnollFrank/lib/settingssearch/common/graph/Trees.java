package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ValueGraph;

import java.util.List;

import de.KnollFrank.lib.settingssearch.common.Lists;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class Trees {

    private Trees() {
    }

    public static <N, V> List<Edge<N, V>> getEdgesOnPath(final TreePath<N, V, ? extends ValueGraph<N, V>> path) {
        return Lists
                .getConsecutivePairs(path.nodes())
                .stream()
                .map(consecutiveNodePair ->
                             EndpointPair.ordered(
                                     consecutiveNodePair.first,
                                     consecutiveNodePair.second))
                .map(edge ->
                             Edge.of(
                                     edge,
                                     path.tree().graph()))
                .toList();
    }
}
