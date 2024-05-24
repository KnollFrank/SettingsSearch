package com.bytehamster.lib.preferencesearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PreferencesGraphProvider {

    private final PreferenceFragmentCompatHelper preferenceFragmentCompatHelper;

    public PreferencesGraphProvider(final PreferenceFragmentCompatHelper preferenceFragmentCompatHelper) {
        this.preferenceFragmentCompatHelper = preferenceFragmentCompatHelper;
    }

    public Graph<PreferenceScreenWithHost, DefaultEdge> getPreferencesGraph(final PreferenceFragmentCompat root) {
        final Graph<PreferenceScreenWithHost, DefaultEdge> preferencesGraph = new DefaultDirectedGraph<>(DefaultEdge.class);
        this.preferenceFragmentCompatHelper.initialize(root);
        buildPreferencesGraph(preferencesGraph, PreferenceScreenWithHostFactory.createPreferenceScreenWithHost(root));
        return preferencesGraph;
    }

    private void buildPreferencesGraph(final Graph<PreferenceScreenWithHost, DefaultEdge> preferencesGraph,
                                       final PreferenceScreenWithHost root) {
        preferencesGraph.addVertex(root);
        this
                .getChildren(root)
                .forEach(
                        child -> {
                            Graphs.addEdgeWithVertices(preferencesGraph, root, child);
                            buildPreferencesGraph(preferencesGraph, child);
                        });
    }

    private List<PreferenceScreenWithHost> getChildren(final PreferenceScreenWithHost preferenceScreen) {
        return PreferenceParser
                .getPreferences(preferenceScreen.preferenceScreen)
                .stream()
                .map(Preference::getFragment)
                .filter(Objects::nonNull)
                .map(this.preferenceFragmentCompatHelper::getPreferenceScreenOfFragment)
                .collect(Collectors.toList());
    }
}
