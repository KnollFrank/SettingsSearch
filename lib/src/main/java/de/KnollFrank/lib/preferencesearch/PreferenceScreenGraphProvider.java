package de.KnollFrank.lib.preferencesearch;

import androidx.preference.Preference;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.common.Preferences;

class PreferenceScreenGraphProvider {

    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;

    public PreferenceScreenGraphProvider(final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider) {
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
    }

    public Graph<PreferenceScreenWithHost, PreferenceEdge> getPreferenceScreenGraph(final PreferenceScreenWithHost root) {
        final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph = new DefaultDirectedGraph<>(PreferenceEdge.class);
        buildPreferenceScreenGraph(root, preferenceScreenGraph);
        return preferenceScreenGraph;
    }

    private void buildPreferenceScreenGraph(final PreferenceScreenWithHost root,
                                            final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        if (preferenceScreenGraph.containsVertex(root)) {
            return;
        }
        preferenceScreenGraph.addVertex(root);
        for (final PreferenceScreenWithHost child : getChildren(root)) {
            buildPreferenceScreenGraph(child, preferenceScreenGraph);
            preferenceScreenGraph.addVertex(child);
            preferenceScreenGraph.addEdge(root, child, new PreferenceEdge(null));
        }
    }

    private List<PreferenceScreenWithHost> getChildren(final PreferenceScreenWithHost preferenceScreenWithHost) {
        return Preferences
                .getAllChildren(preferenceScreenWithHost.preferenceScreen)
                .stream()
                .map(Preference::getFragment)
                .filter(Objects::nonNull)
                .map(this.preferenceScreenWithHostProvider::getPreferenceScreenOfFragment)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
