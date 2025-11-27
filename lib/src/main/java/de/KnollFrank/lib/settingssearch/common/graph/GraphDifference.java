package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.Sets;

import org.jgrapht.Graph;

import java.util.Set;
import java.util.stream.Collectors;

public class GraphDifference<V, E> {

    private final Set<V> verticesOnlyInGraph1;
    private final Set<V> verticesOnlyInGraph2;
    private final Set<E> edgesOnlyInGraph1;
    private final Set<E> edgesOnlyInGraph2;

    private GraphDifference(final Graph<V, E> graph1, final Graph<V, E> graph2) {
        verticesOnlyInGraph1 = Sets.difference(graph1.vertexSet(), graph2.vertexSet());
        verticesOnlyInGraph2 = Sets.difference(graph2.vertexSet(), graph1.vertexSet());
        edgesOnlyInGraph1 = Sets.difference(graph1.edgeSet(), graph2.edgeSet());
        edgesOnlyInGraph2 = Sets.difference(graph2.edgeSet(), graph1.edgeSet());
    }

    public static <V, E> GraphDifference<V, E> between(final Graph<V, E> graph1, final Graph<V, E> graph2) {
        return new GraphDifference<>(graph1, graph2);
    }

    public boolean areEqual() {
        return verticesOnlyInGraph1.isEmpty() &&
                verticesOnlyInGraph2.isEmpty() &&
                edgesOnlyInGraph1.isEmpty() &&
                edgesOnlyInGraph2.isEmpty();
    }

    @Override
    public String toString() {
        if (areEqual()) {
            return "Graphs are equal.";
        }

        final StringBuilder sb = new StringBuilder("Graphs are different:\n");

        if (!verticesOnlyInGraph1.isEmpty()) {
            sb.append("  Vertices only in Graph 1 (cached):\n    ")
                    .append(formatSet(verticesOnlyInGraph1))
                    .append("\n");
        }
        if (!verticesOnlyInGraph2.isEmpty()) {
            sb.append("  Vertices only in Graph 2 (from DB):\n    ")
                    .append(formatSet(verticesOnlyInGraph2))
                    .append("\n");
        }
        if (!edgesOnlyInGraph1.isEmpty()) {
            sb.append("  Edges only in Graph 1 (cached):\n    ")
                    .append(formatSet(edgesOnlyInGraph1))
                    .append("\n");
        }
        if (!edgesOnlyInGraph2.isEmpty()) {
            sb.append("  Edges only in Graph 2 (from DB):\n    ")
                    .append(formatSet(edgesOnlyInGraph2))
                    .append("\n");
        }

        return sb.toString();
    }

    private static <T> String formatSet(final Set<T> set) {
        return set
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(",\n    "));
    }
}
