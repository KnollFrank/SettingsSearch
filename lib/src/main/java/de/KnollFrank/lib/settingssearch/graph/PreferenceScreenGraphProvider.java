package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;

public class PreferenceScreenGraphProvider {

    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;
    private final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider;
    private final OnUiThreadRunner onUiThreadRunner;
    private Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph;

    public PreferenceScreenGraphProvider(final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
                                         final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
                                         final OnUiThreadRunner onUiThreadRunner) {
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
        this.preferenceConnected2PreferenceFragmentProvider = preferenceConnected2PreferenceFragmentProvider;
        this.onUiThreadRunner = onUiThreadRunner;
    }

    public Graph<PreferenceScreenWithHost, PreferenceEdge> getPreferenceScreenGraph(final String rootPreferenceFragmentClassName) {
        preferenceScreenGraph = new DefaultDirectedGraph<>(PreferenceEdge.class);
        buildPreferenceScreenGraph(
                preferenceScreenWithHostProvider
                        .getPreferenceScreenWithHostOfFragment(
                                rootPreferenceFragmentClassName,
                                Optional.empty())
                        .orElseThrow());
        return preferenceScreenGraph;
    }

    private void buildPreferenceScreenGraph(final PreferenceScreenWithHost root) {
        if (preferenceScreenGraph.containsVertex(root)) {
            return;
        }
        preferenceScreenGraph.addVertex(root);
        // FK-TODO-START: diesen Code über einen Listener als VertexSetListener ausführen (siehe Shelf)
        onUiThreadRunner.runOnUiThread(() -> {
            // FK-TODO: reactivate
            // final TextView progressText = SearchPreferenceFragment.progressContainer.findViewById(R.id.progressText);
            // progressText.setText("processing " + root.host().getClass().getSimpleName());
            return null;
        });
        // FK-TODO-END
        // FK-TODO: remove sleep
        try {
            Thread.sleep(1000);
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
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
                        .getChildrenRecursively(preferenceScreenWithHost.preferenceScreen())
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        Function.identity(),
                                        preference -> getConnectedPreferenceScreen(preference, preferenceScreenWithHost.host()))));
    }

    private Optional<PreferenceScreenWithHost> getConnectedPreferenceScreen(
            final Preference preference,
            final PreferenceFragmentCompat host) {
        return this
                .getConnectedPreferenceFragment(preference, host)
                .flatMap(
                        fragmentConnectedToPreference ->
                                preferenceScreenWithHostProvider
                                        .getPreferenceScreenWithHostOfFragment(
                                                fragmentConnectedToPreference,
                                                Optional.of(new PreferenceWithHost(preference, host))));
    }

    private Optional<String> getConnectedPreferenceFragment(final Preference preference, final PreferenceFragmentCompat host) {
        return Optional
                .ofNullable(preference.getFragment())
                .or(() -> preferenceConnected2PreferenceFragmentProvider.getClassNameOfConnectedPreferenceFragment(preference, host));
    }
}
