package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;

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
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnected2PreferenceProvider;

public class PreferenceScreenGraphProvider {

    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;
    private final PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider;
    private final PreferenceScreenGraphListener preferenceScreenGraphListener;
    private Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph;

    public PreferenceScreenGraphProvider(final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
                                         final PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider,
                                         final PreferenceScreenGraphListener preferenceScreenGraphListener) {
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
        this.preferenceFragmentConnected2PreferenceProvider = preferenceFragmentConnected2PreferenceProvider;
        this.preferenceScreenGraphListener = preferenceScreenGraphListener;
    }

    public Graph<PreferenceScreenWithHost, PreferenceEdge> getPreferenceScreenGraph(final String rootPreferenceFragmentClassName) {
        preferenceScreenGraph = PreferenceScreenGraphFactory.createEmptyPreferenceScreenGraph(preferenceScreenGraphListener);
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
        // FK-TODO: remove sleep
//        try {
//            Thread.sleep(1000);
//        } catch (final InterruptedException e) {
//            throw new RuntimeException(e);
//        }
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
                .or(() ->
                        preferenceFragmentConnected2PreferenceProvider
                                .getPreferenceFragmentConnected2Preference(preference, host)
                                .map(Class::getName));
    }
}
