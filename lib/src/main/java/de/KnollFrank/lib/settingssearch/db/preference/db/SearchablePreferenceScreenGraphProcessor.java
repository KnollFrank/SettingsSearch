package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.os.PersistableBundle;

import androidx.fragment.app.FragmentActivity;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;

@FunctionalInterface
public interface SearchablePreferenceScreenGraphProcessor {

    SearchablePreferenceScreenGraph processGraph(SearchablePreferenceScreenGraph graph,
                                                 PersistableBundle actualConfiguration,
                                                 FragmentActivity activityContext);
}
