package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;

import java.util.Optional;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class SubtreeReplacer {

    public static <N, V> Tree<N, V, ImmutableValueGraph<N, V>> replaceSubtreeWithTree(
            final Subtree<N, V, ImmutableValueGraph<N, V>> subtreeToReplace,
            final Tree<N, V, ImmutableValueGraph<N, V>> replacementTree) {
        final MutableValueGraph<N, V> resultGraph = Graphs.toMutableValueGraph(subtreeToReplace.tree().graph());

        // 2. Finde den Elternteil und den Wert der eingehenden Kante, BEVOR der Teilbaum gelöscht wird.
        final Optional<Edge<N, V>> incomingEdge = subtreeToReplace.tree().incomingEdgeOf(subtreeToReplace.rootNodeOfSubtree());

        // 3. Entferne den zu ersetzenden Teilbaum (alle seine Knoten).
        //    Guava entfernt automatisch alle anliegenden Kanten.
        subtreeToReplace.getSubtreeNodes().forEach(resultGraph::removeNode);

        // 4. Füge die Knoten und Kanten des neuen "Ersatz"-Baumes hinzu.
        replacementTree.graph().nodes().forEach(resultGraph::addNode);
        for (final EndpointPair<N> edge : replacementTree.graph().edges()) {
            final V value = replacementTree.graph().edgeValueOrDefault(edge, null);
            // Guava-Kantenwerte sind nie null, daher kann die Prüfung entfallen, wenn der Graph valide ist.
            resultGraph.putEdgeValue(edge.source(), edge.target(), value);
        }

        // 5. Verbinde den ursprünglichen Elternteil mit der Wurzel des neuen Baumes.
        incomingEdge.ifPresent(
                _incomingEdge -> {
                    final N parent = _incomingEdge.endpointPair().source();
                    final V edgeValueToParent = _incomingEdge.value();
                    // Füge den Elternknoten hinzu, falls er durch das Löschen entfernt wurde (falls er Teil des Subtrees war).
                    // Dies ist ein Sicherheitsnetz, sollte aber bei einem validen Baum nicht passieren.
                    resultGraph.addNode(parent);
                    resultGraph.putEdgeValue(parent, replacementTree.rootNode(), edgeValueToParent);
                });
        return new Tree<>(ImmutableValueGraph.copyOf(resultGraph));
    }
}
