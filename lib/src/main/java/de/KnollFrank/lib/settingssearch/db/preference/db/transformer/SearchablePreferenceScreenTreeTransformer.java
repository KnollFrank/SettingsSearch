package de.KnollFrank.lib.settingssearch.db.preference.db.transformer;

import androidx.fragment.app.FragmentActivity;

import com.google.common.graph.ImmutableValueGraph;

import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;

public interface SearchablePreferenceScreenTreeTransformer<C> extends TreeProcessor {

    @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
    Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> transformSearchablePreferenceScreenTree(
            SearchablePreferenceScreenTree<C> searchablePreferenceScreenTree,
            // FK-FIXME: für targetConfiguration wird manchmal null übergeben, was bei mir verboten ist.
            C targetConfiguration,
            FragmentActivity activityContext);
}
