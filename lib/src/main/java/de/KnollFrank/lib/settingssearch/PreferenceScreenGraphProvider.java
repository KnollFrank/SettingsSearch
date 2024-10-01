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
import de.KnollFrank.lib.settingssearch.provider.ISearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;
import de.KnollFrank.lib.settingssearch.provider.PreferenceConnected2PreferenceFragmentProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

class PreferenceScreenGraphProvider {

    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;
    private final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider;
    private final SearchableInfoProvider searchableInfoProvider;
    private final ISearchableDialogInfoOfProvider searchableInfoByPreferenceProvider;
    private Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph;
    private final IsPreferenceSearchable isPreferenceSearchable;

    public PreferenceScreenGraphProvider(final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider,
                                         final PreferenceConnected2PreferenceFragmentProvider preferenceConnected2PreferenceFragmentProvider,
                                         final SearchableInfoProvider searchableInfoProvider,
                                         final ISearchableDialogInfoOfProvider searchableDialogInfoOfProvider,
                                         final IsPreferenceSearchable isPreferenceSearchable) {
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
        this.preferenceConnected2PreferenceFragmentProvider = preferenceConnected2PreferenceFragmentProvider;
        this.searchableInfoProvider = searchableInfoProvider;
        this.searchableInfoByPreferenceProvider = searchableDialogInfoOfProvider;
        this.isPreferenceSearchable = isPreferenceSearchable;
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
        return this
                .getConnectedPreferenceFragment(preference, host)
                .flatMap(
                        fragmentConnectedToPreference ->
                                preferenceScreenWithHostProvider
                                        .getPreferenceScreenOfFragment(
                                                fragmentConnectedToPreference,
                                                Optional.of(new PreferenceWithHost(preference, host)),
                                                searchableInfoProvider,
                                                searchableInfoByPreferenceProvider,
                                                isPreferenceSearchable));
    }

    private Optional<String> getConnectedPreferenceFragment(final Preference preference, final PreferenceFragmentCompat host) {
        return Optional
                .ofNullable(preference.getFragment())
                .or(() -> preferenceConnected2PreferenceFragmentProvider.getClassNameOfConnectedPreferenceFragment(preference, host));
    }
}
