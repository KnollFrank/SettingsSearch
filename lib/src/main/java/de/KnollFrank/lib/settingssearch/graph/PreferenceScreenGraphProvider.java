package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;

import java.util.Map;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
// FK-TODO: rename to PreferenceScreenTreeProvider
class PreferenceScreenGraphProvider {

    private final PreferenceScreenGraphListener preferenceScreenGraphListener;
    private final ConnectedPreferenceScreenByPreferenceProvider connectedPreferenceScreenByPreferenceProvider;
    private final AddEdgeToGraphPredicate addEdgeToGraphPredicate;

    public PreferenceScreenGraphProvider(final PreferenceScreenGraphListener preferenceScreenGraphListener,
                                         final ConnectedPreferenceScreenByPreferenceProvider connectedPreferenceScreenByPreferenceProvider,
                                         final AddEdgeToGraphPredicate addEdgeToGraphPredicate) {
        this.preferenceScreenGraphListener = preferenceScreenGraphListener;
        this.connectedPreferenceScreenByPreferenceProvider = connectedPreferenceScreenByPreferenceProvider;
        this.addEdgeToGraphPredicate = addEdgeToGraphPredicate;
    }

    // FK-TODO: rename to getPreferenceScreenTree()
    public Tree<PreferenceScreenWithHost, Preference, ImmutableValueGraph<PreferenceScreenWithHost, Preference>> getPreferenceScreenGraph(final PreferenceScreenWithHost root) {
        final MutableValueGraph<PreferenceScreenWithHost, Preference> preferenceScreenGraph = PreferenceScreenGraphFactory.createEmptyPreferenceScreenGraph(preferenceScreenGraphListener);
        buildPreferenceScreenGraph(root, preferenceScreenGraph);
        return new Tree<>(ImmutableValueGraph.copyOf(preferenceScreenGraph));
    }

    private void buildPreferenceScreenGraph(final PreferenceScreenWithHost root,
                                            final MutableValueGraph<PreferenceScreenWithHost, Preference> preferenceScreenGraph) {
        if (preferenceScreenGraph.nodes().contains(root)) {
            return;
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
                (final Preference preference, final PreferenceScreenWithHost child) ->
                        addEdgeToGraphPredicate.shallAddEdgeToGraph(
                                new PreferenceEdge(preference),
                                root,
                                child));
    }
}
