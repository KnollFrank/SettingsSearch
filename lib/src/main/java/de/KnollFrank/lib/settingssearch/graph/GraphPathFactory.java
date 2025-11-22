package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;

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
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class GraphPathFactory {

    private final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider;

    public GraphPathFactory(final PreferenceScreenWithHostProvider preferenceScreenWithHostProvider) {
        this.preferenceScreenWithHostProvider = preferenceScreenWithHostProvider;
    }

    public GraphPath<PreferenceScreenWithHost, PreferenceEdge> instantiatePojoGraphPath(final GraphPath<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraphPath) {
        final GraphBuilder<PreferenceScreenWithHost, PreferenceEdge, ? extends DefaultDirectedGraph<PreferenceScreenWithHost, PreferenceEdge>> graphBuilder = DefaultDirectedGraph.createBuilder(PreferenceEdge.class);
        switch (pojoGraphPath.getVertexList().size()) {
            case 0 -> {
                return new GraphWalk<>(
                        graphBuilder.build(),
                        List.of(),
                        0);
            }
            case 1 -> {
                final PreferenceScreenWithHost preferenceScreenWithHostOfFragment =
                        preferenceScreenWithHostProvider
                                .getPreferenceScreenWithHostOfFragment(
                                        pojoGraphPath.getStartVertex().host(),
                                        Optional.empty())
                                .orElseThrow();
                final Graph<PreferenceScreenWithHost, PreferenceEdge> graph =
                        graphBuilder
                                .addVertex(preferenceScreenWithHostOfFragment)
                                .build();
                final GraphPath<PreferenceScreenWithHost, PreferenceEdge> graphPath =
                        new GraphWalk<>(
                                graph,
                                List.of(preferenceScreenWithHostOfFragment),
                                0);
                return graphPath;
            }
            case 2 -> {
                final PreferenceScreenWithHost preferenceScreenWithHost1 =
                        preferenceScreenWithHostProvider
                                .getPreferenceScreenWithHostOfFragment(
                                        pojoGraphPath.getVertexList().get(0).host(),
                                        Optional.empty())
                                .orElseThrow();
                graphBuilder.addVertex(preferenceScreenWithHost1);

                final SearchablePreference searchablePreference =
                        pojoGraphPath
                                .getGraph()
                                .getEdge(pojoGraphPath.getVertexList().get(0), pojoGraphPath.getVertexList().get(1))
                                .preference;
                final PreferenceScreenWithHost preferenceScreenWithHost2 =
                        preferenceScreenWithHostProvider
                                .getPreferenceScreenWithHostOfFragment(
                                        pojoGraphPath.getVertexList().get(1).host(),
                                        Optional.of(
                                                new PreferenceWithHost(
                                                        getPreference(preferenceScreenWithHost1, searchablePreference),
                                                        preferenceScreenWithHost1.host())))
                                .orElseThrow();
                graphBuilder.addVertex(preferenceScreenWithHost2);

                final Graph<PreferenceScreenWithHost, PreferenceEdge> graph = graphBuilder.build();
                final GraphPath<PreferenceScreenWithHost, PreferenceEdge> graphPath =
                        new GraphWalk<>(
                                graph,
                                List.of(preferenceScreenWithHost1, preferenceScreenWithHost2),
                                0);
                return graphPath;
            }
            default -> {
                return null;
            }
        }
    }

    private Preference getPreference(final PreferenceScreenWithHost preferenceScreenWithHost,
                                     final SearchablePreference searchablePreference) {
        final Optional<Preference> preference =
                Optional.ofNullable(
                        preferenceScreenWithHost
                                .preferenceScreen()
                                .findPreference(searchablePreference.getKey()));
        return preference.orElseThrow();
    }
}
