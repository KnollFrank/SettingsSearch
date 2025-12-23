package de.KnollFrank.lib.settingssearch.graph;

import static de.KnollFrank.lib.settingssearch.common.graph.PredecessorOfPreferencesOfNodeSetter.setPredecessorOfPreferencesOfNode;

import org.jgrapht.Graph;
import org.jgrapht.traverse.BreadthFirstIterator;

import de.KnollFrank.lib.settingssearch.common.graph.Graphs;
import de.KnollFrank.lib.settingssearch.common.graph.SearchablePreferenceScreenNodeReplacer;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class GraphMerger {

    // FK-TODO: refactor
    public Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> mergePartialGraphIntoGraph(
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> partialGraph,
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
            final SearchablePreferenceScreen mergePointOfGraph) {
        final SearchablePreferenceScreen rootOfPartialGraph = Graphs.getRootNode(partialGraph).orElseThrow();

        // 2. Struktureller Austausch im Baum.
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> result =
                SearchablePreferenceScreenNodeReplacer.replaceNode(
                        graph,
                        mergePointOfGraph,
                        rootOfPartialGraph);

        // 3. Teilbäume umhängen.
        // Ursprüngliche Kinder des Merge-Points wieder an die neue Wurzel hängen.
        for (final SearchablePreferenceEdge edge : graph.outgoingEdgesOf(mergePointOfGraph)) {
            attachSubtree(graph, edge, result, rootOfPartialGraph);
        }
        // Kinder aus dem Teilgraphen an die neue Wurzel hängen.
        for (final SearchablePreferenceEdge edge : partialGraph.outgoingEdgesOf(rootOfPartialGraph)) {
            attachSubtree(partialGraph, edge, result, rootOfPartialGraph);
        }
        // 4. Globaler Refresh der internen Referenzen (predecessorId).
        // refreshAllPredecessors(result);
        return result;
    }

    private void refreshAllPredecessors(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph) {
        new BreadthFirstIterator<>(graph).forEachRemaining(node -> setPredecessorOfPreferencesOfNode(graph, node));
    }

    private void attachSubtree(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> srcGraph,
                               final SearchablePreferenceEdge edgeToAttach,
                               final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> dstGraph,
                               final SearchablePreferenceScreen attachmentPoint) {

        final SearchablePreferenceScreen subtreeRoot = srcGraph.getEdgeTarget(edgeToAttach);

        // Füge Knoten des Subtrees hinzu, außer sie existieren bereits (Identität über equals/id).
        new BreadthFirstIterator<>(srcGraph, subtreeRoot).forEachRemaining(node -> {
            if (!dstGraph.containsVertex(node)) {
                dstGraph.addVertex(node);
            }
        });

        // Kanten kopieren
        for (final SearchablePreferenceEdge edge : srcGraph.edgeSet()) {
            final SearchablePreferenceScreen source = srcGraph.getEdgeSource(edge);
            final SearchablePreferenceScreen target = srcGraph.getEdgeTarget(edge);

            // Wir kopieren nur Kanten, deren Knoten im Zielgraphen sind und die noch nicht existieren.
            if (dstGraph.containsVertex(source) && dstGraph.containsVertex(target) && !dstGraph.containsEdge(source, target)) {
                dstGraph.addEdge(source, target, edge/*createEdgeFromContext(source, edge)*/);
            }
        }

        // Die verbindende Kante zur neuen Wurzel herstellen.
        if (!dstGraph.containsEdge(attachmentPoint, subtreeRoot)) {
            dstGraph.addEdge(attachmentPoint, subtreeRoot, edgeToAttach/*createEdgeFromContext(attachmentPoint, edgeToAttach)*/);
        }
    }

    private SearchablePreferenceEdge createEdgeFromContext(final SearchablePreferenceScreen sourceNode,
                                                           final SearchablePreferenceEdge template) {
        // WICHTIG: Die Kante muss die Preference-Instanz nutzen, die physikalisch im Parent-Knoten liegt.
        final String key = template.preference.getKey();
        final SearchablePreference preferenceInContext = sourceNode
                .allPreferencesOfPreferenceHierarchy()
                .stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElse(template.preference);

        return new SearchablePreferenceEdge(preferenceInContext);
    }
}
