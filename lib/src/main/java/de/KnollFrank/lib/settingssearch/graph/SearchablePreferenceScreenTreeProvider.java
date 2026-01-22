package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;

import com.google.common.graph.ImmutableValueGraph;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class SearchablePreferenceScreenTreeProvider {

    private final ComputePreferencesListener computePreferencesListener;
    private final TreeToPojoTreeTransformer treeToPojoTreeTransformer;
    private final TreeBuilder<PreferenceScreenWithHost, Preference> treeBuilder;
    private final Locale locale;

    public SearchablePreferenceScreenTreeProvider(final ComputePreferencesListener computePreferencesListener,
                                                  final TreeToPojoTreeTransformer treeToPojoTreeTransformer,
                                                  final TreeBuilder<PreferenceScreenWithHost, Preference> treeBuilder,
                                                  final Locale locale) {
        this.computePreferencesListener = computePreferencesListener;
        this.treeToPojoTreeTransformer = treeToPojoTreeTransformer;
        this.treeBuilder = treeBuilder;
        this.locale = locale;
    }

    public Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> getSearchablePreferenceScreenTree(final PreferenceScreenWithHost root) {
        computePreferencesListener.onStartComputePreferences();
        final var searchablePreferenceScreenGraph = transformGraphToPojoGraph(treeBuilder.buildTreeWithRoot(root));
        computePreferencesListener.onFinishComputePreferences();
        return searchablePreferenceScreenGraph;
    }

    private Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> transformGraphToPojoGraph(
            final Tree<PreferenceScreenWithHost, Preference, ImmutableValueGraph<PreferenceScreenWithHost, Preference>> preferenceScreenGraph) {
        return MapFromPojoNodesRemover.removeMapFromPojoNodes(
                treeToPojoTreeTransformer.transformTreeToPojoTree(preferenceScreenGraph, locale));
    }
}
