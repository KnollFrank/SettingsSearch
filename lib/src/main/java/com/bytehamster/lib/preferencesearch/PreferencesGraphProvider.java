package com.bytehamster.lib.preferencesearch;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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

    private final FragmentActivity fragmentActivity;

    public PreferencesGraphProvider(final FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    public Graph<PreferenceScreenWithHost, DefaultEdge> getPreferencesGraph(final PreferenceFragmentCompat root) {
        final Graph<PreferenceScreenWithHost, DefaultEdge> preferencesGraph = new DefaultDirectedGraph<>(DefaultEdge.class);
        initialize(root);
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

    private void initialize(final Fragment fragment) {
        this
                .fragmentActivity
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment)
                .commitNow();
    }

    private List<PreferenceScreenWithHost> getChildren(final PreferenceScreenWithHost preferenceScreen) {
        return PreferenceParser
                .getPreferences(preferenceScreen.preferenceScreen)
                .stream()
                .map(Preference::getFragment)
                .filter(Objects::nonNull)
                .map(this::getPreferenceScreenOfFragment)
                .collect(Collectors.toList());
    }

    private PreferenceScreenWithHost getPreferenceScreenOfFragment(final String fragment) {
        final PreferenceFragmentCompat preferenceFragmentCompat =
                (PreferenceFragmentCompat) Fragment.instantiate(
                        this.fragmentActivity,
                        fragment,
                        null);
        initialize(preferenceFragmentCompat);
        return PreferenceScreenWithHostFactory.createPreferenceScreenWithHost(preferenceFragmentCompat);
    }
}
