package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.collect.MoreCollectors;

import org.jgrapht.Graph;
import org.jgrapht.traverse.BreadthFirstIterator;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.graph.Graphs;
import de.KnollFrank.lib.settingssearch.common.graph.SearchablePreferenceScreenNodeReplacer;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferences;

public class GraphMerger {

    public Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> mergePartialGraphIntoGraph(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> partialGraph,
                                                                                                  final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph) {
        final SearchablePreferenceScreen rootOfPartialGraph = Graphs.getRootNode(partialGraph).orElseThrow();
        final SearchablePreferenceScreen rootOfGraph = Graphs.getRootNode(graph).orElseThrow();
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> result =
                SearchablePreferenceScreenNodeReplacer.replaceNode(
                        graph,
                        rootOfGraph,
                        rootOfPartialGraph);
        attachSubtree(
                graph,
                getEdgeHavingPreference(
                        graph.outgoingEdgesOf(rootOfGraph),
                        SearchablePreferences
                                .findPreferenceByKey(rootOfGraph.allPreferencesOfPreferenceHierarchy(), "some preference key")
                                .orElseThrow()),
                result,
                rootOfPartialGraph);
        attachSubtree(
                partialGraph,
                getEdgeHavingPreference(
                        partialGraph.outgoingEdgesOf(rootOfPartialGraph),
                        SearchablePreferences
                                .findPreferenceByKey(rootOfPartialGraph.allPreferencesOfPreferenceHierarchy(), "key1")
                                .orElseThrow()),
                result,
                rootOfPartialGraph);
        return result;
    }

    // FK-TODO: refactor
    private void attachSubtree(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> srcGraph,
                               final SearchablePreferenceEdge edgeToAttach,
                               final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> dstGraph,
                               final SearchablePreferenceScreen attachmentPoint) {
        // 1. Das Ziel der Kante ist die Wurzel des Teilbaums, den wir kopieren wollen
        final SearchablePreferenceScreen subtreeRoot = srcGraph.getEdgeTarget(edgeToAttach);

        // 2. Alle Knoten des Teilbaums (inklusive subtreeRoot) in den Zielgraphen kopieren
        new BreadthFirstIterator<>(srcGraph, subtreeRoot).forEachRemaining(dstGraph::addVertex);

        // 3. Alle Kanten innerhalb dieses Teilbaums kopieren
        // Wir iterieren über alle Kanten des Quellgraphen und kopieren die,
        // deren Start- und Zielknoten nun im Zielgraphen existieren (und Teil des Teilbaums sind)
        for (final SearchablePreferenceEdge edge : srcGraph.edgeSet()) {
            final SearchablePreferenceScreen source = srcGraph.getEdgeSource(edge);
            final SearchablePreferenceScreen target = srcGraph.getEdgeTarget(edge);

            // Wenn beide Knoten im Zielgraphen sind, aber die Kante noch fehlt
            if (dstGraph.containsVertex(source) && dstGraph.containsVertex(target) && !dstGraph.containsEdge(source, target)) {
                dstGraph.addEdge(source, target, cloneEdge(edge));
            }
        }

        // 4. Die verbindende Kante (edgeHavingPreference) neu erstellen
        // Sie verbindet den attachmentPoint (rootOfPartialGraph) mit der subtreeRoot
        dstGraph.addEdge(attachmentPoint, subtreeRoot, cloneEdge(edgeToAttach));
    }

    private SearchablePreferenceEdge cloneEdge(SearchablePreferenceEdge edge) {
        // Nutzen Sie hier Ihren Mechanismus zum Klonen (z.B. neuen Konstruktor)
        return new SearchablePreferenceEdge(edge.preference);
    }

    private static SearchablePreferenceEdge getEdgeHavingPreference(final Set<SearchablePreferenceEdge> edges,
                                                                    final SearchablePreference preference) {
        return edges
                .stream()
                .filter(edge -> edge.preference.equals(preference))
                .collect(MoreCollectors.onlyElement());
    }
}
