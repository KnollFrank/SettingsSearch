package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;

import com.google.common.graph.ImmutableValueGraph;

import de.KnollFrank.lib.settingssearch.PreferenceScreenOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.common.LanguageCode;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class SearchablePreferenceScreenTreeProvider {

    private final TreeToPojoTreeTransformer treeToPojoTreeTransformer;
    private final TreeBuilder<PreferenceScreenOfHostOfActivity, Preference> treeBuilder;
    private final LanguageCode languageCode;

    public SearchablePreferenceScreenTreeProvider(final TreeToPojoTreeTransformer treeToPojoTreeTransformer,
                                                  final TreeBuilder<PreferenceScreenOfHostOfActivity, Preference> treeBuilder,
                                                  final LanguageCode languageCode) {
        this.treeToPojoTreeTransformer = treeToPojoTreeTransformer;
        this.treeBuilder = treeBuilder;
        this.languageCode = languageCode;
    }

    public Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> getSearchablePreferenceScreenTree(final PreferenceScreenOfHostOfActivity root) {
        return transformGraphToPojoGraph(treeBuilder.buildTreeWithRoot(root));
    }

    private Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> transformGraphToPojoGraph(
            final Tree<PreferenceScreenOfHostOfActivity, Preference, ImmutableValueGraph<PreferenceScreenOfHostOfActivity, Preference>> preferenceScreenGraph) {
        return MapFromPojoNodesRemover.removeMapFromPojoNodes(
                treeToPojoTreeTransformer.transformTreeToPojoTree(preferenceScreenGraph, languageCode));
    }
}
