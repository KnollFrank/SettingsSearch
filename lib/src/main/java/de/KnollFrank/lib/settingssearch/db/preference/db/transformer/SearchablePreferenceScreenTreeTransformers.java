package de.KnollFrank.lib.settingssearch.db.preference.db.transformer;

import android.os.PersistableBundle;

import androidx.fragment.app.FragmentActivity;

import com.google.common.graph.ImmutableValueGraph;

import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;

public class SearchablePreferenceScreenTreeTransformers {

    public static <C> SearchablePreferenceScreenTreeTransformer<C> identityTreeTransformer() {
        return new SearchablePreferenceScreenTreeTransformer<>() {

            @Override
            @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
            public Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> transformSearchablePreferenceScreenTree(
                    final SearchablePreferenceScreenTree<C> searchablePreferenceScreenTree,
                    final C targetConfiguration,
                    final FragmentActivity activityContext) {
                return searchablePreferenceScreenTree.tree();
            }

            @Override
            public PersistableBundle getParams() {
                return new PersistableBundle();
            }
        };
    }
}
