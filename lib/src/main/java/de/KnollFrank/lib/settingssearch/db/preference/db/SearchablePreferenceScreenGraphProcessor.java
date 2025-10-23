package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;

@FunctionalInterface
public interface SearchablePreferenceScreenGraphProcessor<C> {

    SearchablePreferenceScreenGraph processGraph(SearchablePreferenceScreenGraph graph,
                                                 C actualConfiguration,
                                                 FragmentActivity activityContext);
}
