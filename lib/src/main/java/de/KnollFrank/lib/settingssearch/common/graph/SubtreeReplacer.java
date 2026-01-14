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
        // FK-TODO: use de.KnollFrank.lib.settingssearch.common.graph.Edge record
        final Optional<EndpointPair<N>> incomingEdge = subtreeToReplace.tree().incomingEdgeOf(subtreeToReplace.rootNodeOfSubtree());
        final Optional<N> parent = incomingEdge.map(EndpointPair::source);
        final Optional<V> edgeValueToParent = incomingEdge.map(edge -> subtreeToReplace.tree().graph().edgeValueOrDefault(edge, null));

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
        parent.ifPresent(
                _parent ->
                        edgeValueToParent.ifPresent(
                                _edgeValueToParent -> {
                                    // Füge den Elternknoten hinzu, falls er durch das Löschen entfernt wurde (falls er Teil des Subtrees war).
                                    // Dies ist ein Sicherheitsnetz, sollte aber bei einem validen Baum nicht passieren.
                                    resultGraph.addNode(_parent);
                                    resultGraph.putEdgeValue(_parent, replacementTree.rootNode(), _edgeValueToParent);
                                })
                        );
        return new Tree<>(ImmutableValueGraph.copyOf(resultGraph));
    }
}
