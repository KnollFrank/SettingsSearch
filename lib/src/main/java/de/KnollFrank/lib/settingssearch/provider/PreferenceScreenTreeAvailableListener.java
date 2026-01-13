package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.Preference;

import com.google.common.graph.ImmutableValueGraph;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;

@FunctionalInterface
public interface PreferenceScreenTreeAvailableListener {

    @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
    void onPreferenceScreenTreeAvailable(Tree<PreferenceScreenWithHost, Preference, ImmutableValueGraph<PreferenceScreenWithHost, Preference>> preferenceScreenTree);
}
