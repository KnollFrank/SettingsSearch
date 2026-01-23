package de.KnollFrank.lib.settingssearch.db.preference.db.transformer;

import androidx.fragment.app.FragmentActivity;

import com.google.common.graph.ImmutableValueGraph;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

@FunctionalInterface
public interface SearchablePreferenceScreenTreeCreator<C> {

    @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
    Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> createTree(
            Locale locale,
            // FK-FIXME: für targetConfiguration wird manchmal null übergeben, was bei mir verboten ist.
            C targetConfiguration,
            FragmentActivity activityContext);
}
