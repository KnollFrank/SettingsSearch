package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;

import java.util.function.Function;
import java.util.function.Supplier;

public class NodeReplacer<V, E> {

    private final Supplier<Graph<V, E>> graphFactory;
    private final Function<E, E> cloneEdge;

    public NodeReplacer(final Supplier<Graph<V, E>> graphFactory,
                        final Function<E, E> cloneEdge) {
        this.graphFactory = graphFactory;
        this.cloneEdge = cloneEdge;
    }

    /**
     * Erstellt einen neuen Graphen, in dem ein einzelner Knoten durch einen neuen Knoten ersetzt wurde.
     * Alle eingehenden und ausgehenden Kanten des alten Knotens werden auf den neuen Knoten umgeleitet.
     *
     * @param originalGraph   Der ursprüngliche Graph.
     * @param nodeOfOriginalGraphToReplace   Der zu ersetzende Knoten.
     * @param replacementNode Der neue Knoten.
     * @return Ein neuer Graph mit dem ersetzten Knoten.
     */
    public Graph<V, E> replaceNode(final Graph<V, E> originalGraph,
                                   final V nodeOfOriginalGraphToReplace,
                                   final V replacementNode) {
        // Wenn der zu ersetzende Knoten nicht existiert, eine Kopie des Originalgraphen zurückgeben.
        if (!originalGraph.containsVertex(nodeOfOriginalGraphToReplace)) {
            final Graph<V, E> newGraph = graphFactory.get();
            Graphs.addGraph(newGraph, originalGraph);
            return newGraph;
        }

        final Graph<V, E> newGraph = graphFactory.get();

        // 1. Alle Knoten außer dem alten hinzufügen und stattdessen den neuen Knoten einfügen.
        originalGraph.vertexSet().stream()
                .filter(v -> !v.equals(nodeOfOriginalGraphToReplace))
                .forEach(newGraph::addVertex);
        newGraph.addVertex(replacementNode);

        // 2. Die Kanten des Originalgraphen durchgehen und im neuen Graphen wiederherstellen oder umleiten.
        for (final E edge : originalGraph.edgeSet()) {
            V source = originalGraph.getEdgeSource(edge);
            V target = originalGraph.getEdgeTarget(edge);

            final boolean sourceIsOld = source.equals(nodeOfOriginalGraphToReplace);
            final boolean targetIsOld = target.equals(nodeOfOriginalGraphToReplace);

            final V newSource = sourceIsOld ? replacementNode : source;
            final V newTarget = targetIsOld ? replacementNode : target;

            // Nur Kanten hinzufügen, deren Knoten auch im neuen Graphen vorhanden sind.
            // Dies ist implizit der Fall, da wir alle relevanten Knoten bereits hinzugefügt haben.
            newGraph.addEdge(newSource, newTarget, cloneEdge.apply(edge));
        }

        return newGraph;
    }
}
