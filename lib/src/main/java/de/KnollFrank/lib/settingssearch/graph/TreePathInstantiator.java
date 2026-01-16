package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.ImmutableList;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.common.HeadAndTail;
import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.common.graph.Graphs;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.common.graph.TreePath;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class TreePathInstantiator {

    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;

    public TreePathInstantiator(final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider) {
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
    }

    public TreePath<PreferenceScreenWithHost, Preference, ImmutableValueGraph<PreferenceScreenWithHost, Preference>> instantiate(final TreePath<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> treePath) {
        return instantiateTreePath(
                Lists
                        .asHeadAndTail(treePath.nodes())
                        .orElseThrow(),
                treePath.tree());
    }

    private TreePath<PreferenceScreenWithHost, Preference, ImmutableValueGraph<PreferenceScreenWithHost, Preference>> instantiateTreePath(
            final HeadAndTail<SearchablePreferenceScreen> treePath,
            final Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> tree) {
        final ImmutableValueGraph.Builder<PreferenceScreenWithHost, Preference> graphBuilder = Graphs.directedImmutableValueGraphBuilder();
        final ImmutableList.Builder<PreferenceScreenWithHost> nodesBuilder = ImmutableList.builder();
        SearchablePreferenceScreen searchablePreferenceScreenPrevious = treePath.head();
        PreferenceScreenWithHost preferenceScreenWithHostPrevious =
                preferenceScreenWithHostProvider
                        .getPreferenceScreenWithHostOfFragment(
                                searchablePreferenceScreenPrevious.host(),
                                Optional.empty())
                        .orElseThrow();
        graphBuilder.addNode(preferenceScreenWithHostPrevious);
        nodesBuilder.add(preferenceScreenWithHostPrevious);
        for (final SearchablePreferenceScreen searchablePreferenceScreenActual : treePath.tail()) {
            final Preference preferencePrevious =
                    getInstanceOfSearchablePreference(
                            preferenceScreenWithHostPrevious.host(),
                            getSearchablePreferenceOnEdge(
                                    tree,
                                    EndpointPair.ordered(
                                            searchablePreferenceScreenPrevious,
                                            searchablePreferenceScreenActual)));
            final PreferenceScreenWithHost preferenceScreenWithHostActual =
                    preferenceScreenWithHostProvider
                            .getPreferenceScreenWithHostOfFragment(
                                    searchablePreferenceScreenActual.host(),
                                    Optional.of(
                                            new PreferenceWithHost(
                                                    preferencePrevious,
                                                    preferenceScreenWithHostPrevious.host())))
                            .orElseThrow();
            graphBuilder.addNode(preferenceScreenWithHostActual);
            nodesBuilder.add(preferenceScreenWithHostActual);
            graphBuilder.putEdgeValue(
                    preferenceScreenWithHostPrevious,
                    preferenceScreenWithHostActual,
                    preferencePrevious);
            searchablePreferenceScreenPrevious = searchablePreferenceScreenActual;
            preferenceScreenWithHostPrevious = preferenceScreenWithHostActual;
        }
        return new TreePath<>(
                new Tree<>(graphBuilder.build()),
                nodesBuilder.build());
    }

    private static Preference getInstanceOfSearchablePreference(final PreferenceFragmentCompat hostOfPreference,
                                                                final SearchablePreference searchablePreference) {
        return Preferences.findPreferenceByKeyOrElseThrow(
                hostOfPreference,
                searchablePreference.getKey());
    }

    private static SearchablePreference getSearchablePreferenceOnEdge(
            final Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> tree,
            final EndpointPair<SearchablePreferenceScreen> edge) {
        return tree.graph().edgeValueOrDefault(edge, null);
    }
}
