package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraph;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenTreeAvailableListener;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class SearchablePreferenceScreenTreeProvider {

    private final PreferenceScreenTreeAvailableListener preferenceScreenTreeAvailableListener;
    private final ComputePreferencesListener computePreferencesListener;
    private final TreeToPojoTreeTransformer treeToPojoTreeTransformer;
    private final PreferenceScreenGraphProvider preferenceScreenGraphProvider;
    private final Locale locale;

    public SearchablePreferenceScreenTreeProvider(final PreferenceScreenTreeAvailableListener preferenceScreenTreeAvailableListener,
                                                  final ComputePreferencesListener computePreferencesListener,
                                                  final TreeToPojoTreeTransformer treeToPojoTreeTransformer,
                                                  final PreferenceScreenGraphProvider preferenceScreenGraphProvider,
                                                  final Locale locale) {
        this.preferenceScreenTreeAvailableListener = preferenceScreenTreeAvailableListener;
        this.computePreferencesListener = computePreferencesListener;
        this.treeToPojoTreeTransformer = treeToPojoTreeTransformer;
        this.preferenceScreenGraphProvider = preferenceScreenGraphProvider;
        this.locale = locale;
    }

    public Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> getSearchablePreferenceScreenTree(final PreferenceScreenWithHost root) {
        computePreferencesListener.onStartComputePreferences();
        final var searchablePreferenceScreenGraph = _getSearchablePreferenceScreenTree(root);
        computePreferencesListener.onFinishComputePreferences();
        return searchablePreferenceScreenGraph;
    }

    private Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> _getSearchablePreferenceScreenTree(final PreferenceScreenWithHost root) {
        final Tree<PreferenceScreenWithHost, Preference, ? extends ValueGraph<PreferenceScreenWithHost, Preference>> preferenceScreenTree = preferenceScreenGraphProvider.getPreferenceScreenGraph(root);
        preferenceScreenTreeAvailableListener.onPreferenceScreenTreeAvailable(preferenceScreenTree);
        return transformGraphToPojoGraph(preferenceScreenTree);
    }

    private Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> transformGraphToPojoGraph(
            final Tree<PreferenceScreenWithHost, Preference, ? extends ValueGraph<PreferenceScreenWithHost, Preference>> preferenceScreenGraph) {
        return MapFromPojoNodesRemover.removeMapFromPojoNodes(
                treeToPojoTreeTransformer.transformTreeToPojoTree(preferenceScreenGraph, locale));
    }
}
