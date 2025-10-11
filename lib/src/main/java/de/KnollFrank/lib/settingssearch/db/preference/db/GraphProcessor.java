package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;

@FunctionalInterface
public interface GraphProcessor {

    SearchablePreferenceScreenGraph processGraph(SearchablePreferenceScreenGraph graph, FragmentActivity activityContext);
}
