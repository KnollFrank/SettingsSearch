package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;

import com.google.common.graph.MutableValueGraph;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;

class PreferenceScreenGraphFactory {

    @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
    public static MutableValueGraph<PreferenceScreenWithHost, Preference> createEmptyPreferenceScreenGraph(final PreferenceScreenGraphListener preferenceScreenGraphListener) {
        return new ListenableMutableValueGraph(preferenceScreenGraphListener);
    }
}
