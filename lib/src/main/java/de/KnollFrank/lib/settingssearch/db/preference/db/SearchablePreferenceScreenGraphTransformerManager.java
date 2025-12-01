package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;

class SearchablePreferenceScreenGraphTransformerManager<C> {

    // FK-TODO: graphTransformers in der Suchdatenbank speichern
    private final List<SearchablePreferenceScreenGraphTransformer<C>> graphTransformers = new ArrayList<>();

    public void addGraphTransformer(final SearchablePreferenceScreenGraphTransformer<C> graphTransformer) {
        graphTransformers.add(graphTransformer);
    }

    public void resetGraphTransformers() {
        graphTransformers.clear();
    }

    public List<SearchablePreferenceScreenGraph> executeGraphTransformersOnGraphs(
            final List<SearchablePreferenceScreenGraph> graphs,
            final C actualConfiguration,
            final FragmentActivity activityContext) {
        final List<SearchablePreferenceScreenGraph> transformedGraphs =
                graphs
                        .stream()
                        .map(graph -> executeGraphTransformersOnGraph(graph, actualConfiguration, activityContext))
                        .collect(Collectors.toList());
        resetGraphTransformers();
        return transformedGraphs;
    }

    private SearchablePreferenceScreenGraph executeGraphTransformersOnGraph(
            final SearchablePreferenceScreenGraph graph,
            final C actualConfiguration,
            final FragmentActivity activityContext) {
        return graphTransformers
                .stream()
                .reduce(
                        graph,
                        (currentGraph, graphTransformer) ->
                                graphTransformer.transformGraph(currentGraph, actualConfiguration, activityContext),
                        (graph1, graph2) -> {
                            throw new UnsupportedOperationException("Parallel stream not supported");
                        });
    }
}
