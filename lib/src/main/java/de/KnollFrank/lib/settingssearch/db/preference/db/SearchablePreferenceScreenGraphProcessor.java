package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;

@FunctionalInterface
// FK-TODO: rename to SearchablePreferenceScreenGraphTransformer
public interface SearchablePreferenceScreenGraphProcessor<C> {

    // FK-TODO: rename to transformGraph
    SearchablePreferenceScreenGraph processGraph(SearchablePreferenceScreenGraph graph,
                                                 // FK-FIXME: für actualConfiguration wird manchmal null übergeben, was bei mir verboten ist.
                                                 C actualConfiguration,
                                                 FragmentActivity activityContext);
}
