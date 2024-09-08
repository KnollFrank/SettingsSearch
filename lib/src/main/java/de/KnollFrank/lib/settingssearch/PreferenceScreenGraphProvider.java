package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.common.Preferences;

class PreferenceScreenGraphProvider {

    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;
    private Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph;

    public PreferenceScreenGraphProvider(final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider) {
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
    }

    public Graph<PreferenceScreenWithHost, PreferenceEdge> getPreferenceScreenGraph(final PreferenceScreenWithHost root) {
        preferenceScreenGraph = new DefaultDirectedGraph<>(PreferenceEdge.class);
        buildPreferenceScreenGraph(root);
        return preferenceScreenGraph;
    }

    private void buildPreferenceScreenGraph(final PreferenceScreenWithHost root) {
        if (preferenceScreenGraph.containsVertex(root)) {
            return;
        }
        preferenceScreenGraph.addVertex(root);
        this
                .getConnectedPreferenceScreenByPreference(root)
                .forEach(
                        (preference, child) -> {
                            buildPreferenceScreenGraph(child);
                            preferenceScreenGraph.addVertex(child);
                            preferenceScreenGraph.addEdge(root, child, new PreferenceEdge(preference));
                        });
    }

    private Map<Preference, PreferenceScreenWithHost> getConnectedPreferenceScreenByPreference(final PreferenceScreenWithHost preferenceScreenWithHost) {
        return Maps.filterPresentValues(
                Preferences
                        .getAllChildren(preferenceScreenWithHost.preferenceScreen())
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        Function.identity(),
                                        preference -> getConnectedPreferenceScreen(preference, preferenceScreenWithHost.host()))));
    }

    private Optional<PreferenceScreenWithHost> getConnectedPreferenceScreen(
            final Preference preference,
            final PreferenceFragmentCompat host) {
        final String fragmentConnectedToPreference = preference.getFragment();
        return fragmentConnectedToPreference != null ?
                preferenceScreenWithHostProvider.getPreferenceScreenOfFragment(
                        fragmentConnectedToPreference,
                        Optional.of(new PreferenceWithHost(preference, host))) :
                Optional.empty();
    }
}
