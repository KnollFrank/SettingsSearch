package com.bytehamster.lib.preferencesearch;

import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class PreferencesGraphProvider {

    private final FragmentManager fragmentManager;

    public PreferencesGraphProvider(final FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public Graph<PreferenceScreen, DefaultEdge> getPreferencesGraph(final PreferenceFragmentCompat root) {
        final Graph<PreferenceScreen, DefaultEdge> preferencesGraph = new DefaultDirectedGraph<>(DefaultEdge.class);
        initialize(root);
        preferencesGraph.addVertex(root.getPreferenceScreen());
        return preferencesGraph;
    }

    private void initialize(final PreferenceFragmentCompat root) {
        this
                .fragmentManager
                .beginTransaction()
                .replace(android.R.id.content, root)
                .commitNow();
    }
}
