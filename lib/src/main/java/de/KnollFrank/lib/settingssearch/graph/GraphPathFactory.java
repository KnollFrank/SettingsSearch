package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.GraphWalk;
import org.jgrapht.graph.builder.GraphBuilder;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostProvider;
import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.common.HeadAndTail;
import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class GraphPathFactory {

    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;

    public GraphPathFactory(final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider) {
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
    }

    public GraphPath<PreferenceScreenWithHost, PreferenceEdge> instantiate(final GraphPath<SearchablePreferenceScreen, SearchablePreferenceEdge> graphPath) {
        return Lists
                .asHeadAndTail(graphPath.getVertexList())
                .map(_graphPath -> instantiateGraphPath(_graphPath, graphPath.getGraph()))
                .orElseGet(GraphPathFactory::emptyGraphPath);
    }

    private static GraphWalk<PreferenceScreenWithHost, PreferenceEdge> emptyGraphPath() {
        return new GraphWalk<>(
                new DefaultDirectedGraph<>(PreferenceEdge.class),
                List.of(),
                0);
    }

    private GraphWalk<PreferenceScreenWithHost, PreferenceEdge> instantiateGraphPath(
            final HeadAndTail<SearchablePreferenceScreen> graphPath,
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph) {
        final GraphBuilder<PreferenceScreenWithHost, PreferenceEdge, ? extends DefaultDirectedGraph<PreferenceScreenWithHost, PreferenceEdge>> graphBuilder = DefaultDirectedGraph.createBuilder(PreferenceEdge.class);
        final Builder<PreferenceScreenWithHost> vertexListBuilder = ImmutableList.builder();
        SearchablePreferenceScreen searchablePreferenceScreenPrevious = graphPath.head();
        PreferenceScreenWithHost preferenceScreenWithHostPrevious =
                preferenceScreenWithHostProvider
                        .getPreferenceScreenWithHostOfFragment(
                                searchablePreferenceScreenPrevious.host(),
                                Optional.empty())
                        .orElseThrow();
        graphBuilder.addVertex(preferenceScreenWithHostPrevious);
        vertexListBuilder.add(preferenceScreenWithHostPrevious);
        for (final SearchablePreferenceScreen searchablePreferenceScreenActual : graphPath.tail()) {
            final Preference preferencePrevious =
                    getInstanceOfSearchablePreference(
                            preferenceScreenWithHostPrevious.host(),
                            getSearchablePreferenceConnectingSourceWithTarget(
                                    graph,
                                    searchablePreferenceScreenPrevious,
                                    searchablePreferenceScreenActual));
            final PreferenceScreenWithHost preferenceScreenWithHostActual =
                    preferenceScreenWithHostProvider
                            .getPreferenceScreenWithHostOfFragment(
                                    searchablePreferenceScreenActual.host(),
                                    Optional.of(
                                            new PreferenceWithHost(
                                                    preferencePrevious,
                                                    preferenceScreenWithHostPrevious.host())))
                            .orElseThrow();
            graphBuilder.addVertex(preferenceScreenWithHostActual);
            vertexListBuilder.add(preferenceScreenWithHostActual);
            graphBuilder.addEdge(
                    preferenceScreenWithHostPrevious,
                    preferenceScreenWithHostActual,
                    new PreferenceEdge(preferencePrevious));
            searchablePreferenceScreenPrevious = searchablePreferenceScreenActual;
            preferenceScreenWithHostPrevious = preferenceScreenWithHostActual;
        }
        return new GraphWalk<>(
                graphBuilder.build(),
                vertexListBuilder.build(),
                0);
    }

    private static Preference getInstanceOfSearchablePreference(final PreferenceFragmentCompat hostOfPreference,
                                                                final SearchablePreference searchablePreference) {
        return Preferences.findPreferenceByKeyOrElseThrow(
                hostOfPreference,
                searchablePreference.getKey());
    }

    private static SearchablePreference getSearchablePreferenceConnectingSourceWithTarget(
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
            final SearchablePreferenceScreen source,
            final SearchablePreferenceScreen target) {
        return graph
                .getEdge(source, target)
                .preference;
    }
}
