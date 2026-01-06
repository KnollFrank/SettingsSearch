package de.KnollFrank.lib.settingssearch.db.preference.db.transformer;

import androidx.fragment.app.FragmentActivity;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;

@FunctionalInterface
public interface SearchablePreferenceScreenGraphTransformer<C> {

    SearchablePreferenceScreenGraph transformGraph(SearchablePreferenceScreenGraph graph,
                                                   // FK-FIXME: für actualConfiguration wird manchmal null übergeben, was bei mir verboten ist.
                                                   C actualConfiguration,
                                                   FragmentActivity activityContext);
}
