package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

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
        if (pojoGraphPath.getVertexList().isEmpty()) {
            return new GraphWalk<>(
                    graphBuilder.build(),
                    List.of(),
                    0);
        }
        final Builder<PreferenceScreenWithHost> vertexListBuilder = ImmutableList.builder();
        SearchablePreferenceScreen searchablePreferenceScreenPrevious = pojoGraphPath.getVertexList().get(0);
        PreferenceScreenWithHost preferenceScreenWithHostPrevious =
                preferenceScreenWithHostProvider
                        .getPreferenceScreenWithHostOfFragment(
                                searchablePreferenceScreenPrevious.host(),
                                Optional.empty())
                        .orElseThrow();
        graphBuilder.addVertex(preferenceScreenWithHostPrevious);
        vertexListBuilder.add(preferenceScreenWithHostPrevious);

        for (int i = 1; i < pojoGraphPath.getVertexList().size(); i++) {
            final SearchablePreferenceScreen searchablePreferenceScreenActual = pojoGraphPath.getVertexList().get(i);
            final Preference preferencePrevious =
                    getPreference(
                            preferenceScreenWithHostPrevious,
                            pojoGraphPath
                                    .getGraph()
                                    .getEdge(searchablePreferenceScreenPrevious, searchablePreferenceScreenActual)
                                    .preference);
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
