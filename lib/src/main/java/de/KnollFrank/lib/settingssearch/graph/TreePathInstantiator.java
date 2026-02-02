package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.ImmutableList;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceOfHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
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

    public TreePath<PreferenceScreenOfHostOfActivity, Preference, ImmutableValueGraph<PreferenceScreenOfHostOfActivity, Preference>> instantiate(final TreePath<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> treePath) {
        return instantiateTreePath(
                Lists
                        .asHeadAndTail(treePath.nodes())
                        .orElseThrow(),
                treePath.tree());
    }

    private TreePath<PreferenceScreenOfHostOfActivity, Preference, ImmutableValueGraph<PreferenceScreenOfHostOfActivity, Preference>> instantiateTreePath(
            final HeadAndTail<SearchablePreferenceScreen> treePath,
            final Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> tree) {
        final ImmutableValueGraph.Builder<PreferenceScreenOfHostOfActivity, Preference> graphBuilder = Graphs.directedImmutableValueGraphBuilder();
        final ImmutableList.Builder<PreferenceScreenOfHostOfActivity> nodesBuilder = ImmutableList.builder();
        SearchablePreferenceScreen searchablePreferenceScreenPrevious = treePath.head();
        PreferenceScreenOfHostOfActivity preferenceScreenOfHostOfActivityPrevious =
                preferenceScreenWithHostProvider
                        .getPreferenceScreenWithHostOfFragment(
                                searchablePreferenceScreenPrevious
                                        .host()
                                        .asFragmentClassOfActivity(),
                                Optional.empty())
                        .orElseThrow();
        graphBuilder.addNode(preferenceScreenOfHostOfActivityPrevious);
        nodesBuilder.add(preferenceScreenOfHostOfActivityPrevious);
        for (final SearchablePreferenceScreen searchablePreferenceScreenActual : treePath.tail()) {
            final Preference preferencePrevious =
                    getInstanceOfSearchablePreference(
                            preferenceScreenOfHostOfActivityPrevious.hostOfPreferenceScreen(),
                            getSearchablePreferenceOnEdge(
                                    tree,
                                    EndpointPair.ordered(
                                            searchablePreferenceScreenPrevious,
                                            searchablePreferenceScreenActual)));
            final PreferenceScreenOfHostOfActivity preferenceScreenOfHostOfActivityActual =
                    preferenceScreenWithHostProvider
                            .getPreferenceScreenWithHostOfFragment(
                                    searchablePreferenceScreenActual
                                            .host()
                                            .asFragmentClassOfActivity(),
                                    Optional.of(
                                            new PreferenceOfHost(
                                                    preferencePrevious,
                                                    preferenceScreenOfHostOfActivityPrevious.hostOfPreferenceScreen())))
                            .orElseThrow();
            graphBuilder.addNode(preferenceScreenOfHostOfActivityActual);
            nodesBuilder.add(preferenceScreenOfHostOfActivityActual);
            graphBuilder.putEdgeValue(
                    preferenceScreenOfHostOfActivityPrevious,
                    preferenceScreenOfHostOfActivityActual,
                    preferencePrevious);
            searchablePreferenceScreenPrevious = searchablePreferenceScreenActual;
            preferenceScreenOfHostOfActivityPrevious = preferenceScreenOfHostOfActivityActual;
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
        return Graphs.getEdgeValue(edge, tree.graph());
    }
}
