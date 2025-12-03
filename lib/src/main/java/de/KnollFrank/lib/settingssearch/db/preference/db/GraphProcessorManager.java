package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

import com.codepoetics.ambivalence.Either;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;
import de.KnollFrank.lib.settingssearch.graph.ComputePreferencesListener;

class GraphProcessorManager<C> {

    private final ComputePreferencesListener computePreferencesListener;
    // FK-TODO: graphProcessors in der Suchdatenbank speichern
    // FK-TODO: use Queue instead of List?
    private final List<Either<SearchablePreferenceScreenGraphTransformer<C>, SearchablePreferenceScreenGraphCreator<C>>> graphProcessors = new ArrayList<>();

    public GraphProcessorManager(final ComputePreferencesListener computePreferencesListener) {
        this.computePreferencesListener = computePreferencesListener;
    }

    public void addGraphTransformer(final SearchablePreferenceScreenGraphTransformer<C> graphTransformer) {
        graphProcessors.add(Either.ofLeft(graphTransformer));
    }

    public void addGraphCreator(final SearchablePreferenceScreenGraphCreator<C> graphCreator) {
        removeGraphProcessors();
        graphProcessors.add(Either.ofRight(graphCreator));
    }

    public void removeGraphProcessors() {
        graphProcessors.clear();
    }

    public boolean hasGraphProcessors() {
        return !graphProcessors.isEmpty();
    }

    public List<SearchablePreferenceScreenGraph> applyGraphProcessorsToGraphs(
            final List<SearchablePreferenceScreenGraph> graphs,
            final C configuration,
            final FragmentActivity activityContext) {
        computePreferencesListener.onStartComputePreferences();
        final List<SearchablePreferenceScreenGraph> transformedGraphs =
                graphs
                        .stream()
                        .map(graph -> applyGraphProcessorsToGraph(graph, configuration, activityContext))
                        .collect(Collectors.toList());
        removeGraphProcessors();
        computePreferencesListener.onFinishComputePreferences();
        return transformedGraphs;
    }

    private SearchablePreferenceScreenGraph applyGraphProcessorsToGraph(
            final SearchablePreferenceScreenGraph graph,
            final C configuration,
            final FragmentActivity activityContext) {
        return graphProcessors
                .stream()
                .reduce(
                        graph,
                        (currentGraph, graphProcessor) ->
                                applyGraphProcessorToGraph(
                                        graphProcessor,
                                        currentGraph,
                                        configuration,
                                        activityContext),
                        (graph1, graph2) -> {
                            throw new UnsupportedOperationException("Parallel stream not supported");
                        });
    }

    private static <C> SearchablePreferenceScreenGraph applyGraphProcessorToGraph(
            final Either<SearchablePreferenceScreenGraphTransformer<C>, SearchablePreferenceScreenGraphCreator<C>> graphProcessor,
            final SearchablePreferenceScreenGraph graph,
            final C configuration,
            final FragmentActivity activityContext) {
        return graphProcessor.join(
                graphTransformer -> graphTransformer.transformGraph(graph, configuration, activityContext),
                graphCreator -> graphCreator.createGraph(graph.locale(), configuration, activityContext));
    }
}
