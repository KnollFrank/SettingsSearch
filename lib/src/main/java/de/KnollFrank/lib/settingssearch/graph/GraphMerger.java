package de.KnollFrank.lib.settingssearch.graph;

import static de.KnollFrank.lib.settingssearch.common.graph.PredecessorOfPreferencesOfNodeSetter.setPredecessorOfPreferencesOfNode;

import org.jgrapht.Graph;
import org.jgrapht.traverse.BreadthFirstIterator;

import de.KnollFrank.lib.settingssearch.common.graph.Graphs;
import de.KnollFrank.lib.settingssearch.common.graph.SearchablePreferenceScreenNodeReplacer;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class GraphMerger {

    public Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> mergePartialGraphIntoGraph(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> partialGraph,
                                                                                                  final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph) {
        final SearchablePreferenceScreen rootOfPartialGraph = Graphs.getRootNode(partialGraph).orElseThrow();
        final SearchablePreferenceScreen rootOfGraph = Graphs.getRootNode(graph).orElseThrow();

        // 1. Erstelle den Basis-Graphen, indem die alte Wurzel durch die neue ersetzt wird.
        // Alle internen Strukturen von 'graph' bleiben erhalten, die Wurzel ist nun 'rootOfPartialGraph'.
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> result =
                SearchablePreferenceScreenNodeReplacer.replaceNode(
                        graph,
                        rootOfGraph,
                        rootOfPartialGraph);

        // 2. Hänge ALLE Teilbäume an, die im ursprünglichen 'graph' an der alten Wurzel hingen.
        for (final SearchablePreferenceEdge edge : graph.outgoingEdgesOf(rootOfGraph)) {
            attachSubtree(graph, edge, result, rootOfPartialGraph);
        }

        // 3. Hänge ALLE Teilbäume an, die im 'partialGraph' an dessen Wurzel hängen.
        for (final SearchablePreferenceEdge edge : partialGraph.outgoingEdgesOf(rootOfPartialGraph)) {
            attachSubtree(partialGraph, edge, result, rootOfPartialGraph);
        }

        return result;
    }

    // FK-TODO: refactor
    private void attachSubtree(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> srcGraph,
                               final SearchablePreferenceEdge edgeToAttach,
                               final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> dstGraph,
                               final SearchablePreferenceScreen attachmentPoint) {
        // Das Ziel der Kante im Quellgraph identifizieren
        final SearchablePreferenceScreen subtreeRoot = srcGraph.getEdgeTarget(edgeToAttach);

        // Alle Knoten des Teilbaums (inklusive subtreeRoot) in den Zielgraphen kopieren
        new BreadthFirstIterator<>(srcGraph, subtreeRoot).forEachRemaining(dstGraph::addVertex);

        // Alle Kanten innerhalb dieses Teilbaums im Zielgraphen wiederherstellen
        for (final SearchablePreferenceEdge edge : srcGraph.edgeSet()) {
            final SearchablePreferenceScreen source = srcGraph.getEdgeSource(edge);
            final SearchablePreferenceScreen target = srcGraph.getEdgeTarget(edge);

            // Wenn die Kante Teil des kopierten Teilbaums ist
            if (dstGraph.containsVertex(source) && dstGraph.containsVertex(target) && !dstGraph.containsEdge(source, target)) {
                dstGraph.addEdge(source, target, cloneEdge(edge));
            }
        }

        // Die "Brückenkante" vom neuen attachmentPoint zur Wurzel des Teilbaums schlagen
        dstGraph.addEdge(attachmentPoint, subtreeRoot, cloneEdge(edgeToAttach));
        setPredecessorOfPreferencesOfNode(dstGraph, subtreeRoot);
    }

    private SearchablePreferenceEdge cloneEdge(final SearchablePreferenceEdge edge) {
        return new SearchablePreferenceEdge(edge.preference);
    }
}
