package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
class ListenableMutableValueGraph extends ForwardingMutableValueGraph<PreferenceScreenWithHost, Preference> {

    private final MutableValueGraph<PreferenceScreenWithHost, Preference> delegate;
    private final PreferenceScreenGraphListener listener;

    public ListenableMutableValueGraph(final PreferenceScreenGraphListener listener) {
        this.delegate = ValueGraphBuilder.directed().build();
        this.listener = listener;
    }

    @Override
    protected MutableValueGraph<PreferenceScreenWithHost, Preference> delegate() {
        return delegate;
    }

    @Override
    public boolean addNode(final PreferenceScreenWithHost node) {
        final boolean added = super.addNode(node);
        if (added) {
            listener.preferenceScreenWithHostAdded(node);
        }
        return added;
    }
}
