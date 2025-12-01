package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;

// FK-TODO: rename to SearchablePreferenceScreenGraphTransformerManager
class SearchablePreferenceScreenGraphProcessorManager<C> {

    // FK-TODO: graphProcessors in der Suchdatenbank speichern
    private final List<SearchablePreferenceScreenGraphProcessor<C>> graphProcessors = new ArrayList<>();

    public void addGraphProcessor(final SearchablePreferenceScreenGraphProcessor<C> graphProcessor) {
        graphProcessors.add(graphProcessor);
    }

    public void resetGraphProcessors() {
        graphProcessors.clear();
    }

    public List<SearchablePreferenceScreenGraph> executeGraphProcessorsOnGraphs(
            final List<SearchablePreferenceScreenGraph> graphs,
            final C actualConfiguration,
            final FragmentActivity activityContext) {
        final List<SearchablePreferenceScreenGraph> processedGraphs =
                graphs
                        .stream()
                        .map(graph -> executeGraphProcessorsOnGraph(graph, actualConfiguration, activityContext))
                        .collect(Collectors.toList());
        resetGraphProcessors();
        return processedGraphs;
    }

    private SearchablePreferenceScreenGraph executeGraphProcessorsOnGraph(
            final SearchablePreferenceScreenGraph graph,
            final C actualConfiguration,
            final FragmentActivity activityContext) {
        return graphProcessors
                .stream()
                .reduce(
                        graph,
                        (currentGraph, graphProcessor) ->
                                graphProcessor.processGraph(currentGraph, actualConfiguration, activityContext),
                        (graph1, graph2) -> {
                            throw new UnsupportedOperationException("Parallel stream not supported");
                        });
    }
}
