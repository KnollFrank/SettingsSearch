package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;

import java.util.Map;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.common.graph.Edge;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
class PreferenceScreenTreeProvider {

    private final PreferenceScreenGraphListener preferenceScreenGraphListener;
    private final ConnectedPreferenceScreenByPreferenceProvider connectedPreferenceScreenByPreferenceProvider;
    private final AddEdgeToTreePredicate addEdgeToTreePredicate;

    public PreferenceScreenTreeProvider(final PreferenceScreenGraphListener preferenceScreenGraphListener,
                                        final ConnectedPreferenceScreenByPreferenceProvider connectedPreferenceScreenByPreferenceProvider,
                                        final AddEdgeToTreePredicate addEdgeToTreePredicate) {
        this.preferenceScreenGraphListener = preferenceScreenGraphListener;
        this.connectedPreferenceScreenByPreferenceProvider = connectedPreferenceScreenByPreferenceProvider;
        this.addEdgeToTreePredicate = addEdgeToTreePredicate;
    }

    public Tree<PreferenceScreenWithHost, Preference, ImmutableValueGraph<PreferenceScreenWithHost, Preference>> getPreferenceScreenTree(final PreferenceScreenWithHost root) {
        final MutableValueGraph<PreferenceScreenWithHost, Preference> preferenceScreenGraph = PreferenceScreenGraphFactory.createEmptyPreferenceScreenGraph(preferenceScreenGraphListener);
        buildPreferenceScreenGraph(root, preferenceScreenGraph);
        return new Tree<>(ImmutableValueGraph.copyOf(preferenceScreenGraph));
    }

    private void buildPreferenceScreenGraph(final PreferenceScreenWithHost root,
                                            final MutableValueGraph<PreferenceScreenWithHost, Preference> preferenceScreenGraph) {
        if (preferenceScreenGraph.nodes().contains(root)) {
            throw new IllegalStateException(
                    String.format(
                            "Cycle detected in the preference screen graph. The node '%s' has been visited twice. A tree structure must not contain cycles. Please check your PreferenceFragment hierarchy for circular dependencies.",
                            root));
        }
        preferenceScreenGraph.addNode(root);
        this
                .getConnectedPreferenceScreenByPreference(root)
                .forEach(
                        (preference, child) -> {
                            buildPreferenceScreenGraph(child, preferenceScreenGraph);
                            preferenceScreenGraph.addNode(child);
                            preferenceScreenGraph.putEdgeValue(
                                    EndpointPair.ordered(root, child),
                                    preference);
                        });
    }

    private Map<Preference, PreferenceScreenWithHost> getConnectedPreferenceScreenByPreference(final PreferenceScreenWithHost root) {
        return Maps.filter(
                connectedPreferenceScreenByPreferenceProvider.getConnectedPreferenceScreenByPreference(root),
                (preference, connectedPreferenceScreen) ->
                        addEdgeToTreePredicate.shallAddEdgeToTree(
                                new Edge<>(
                                        EndpointPair.ordered(root, connectedPreferenceScreen),
                                        preference)));
    }
}
