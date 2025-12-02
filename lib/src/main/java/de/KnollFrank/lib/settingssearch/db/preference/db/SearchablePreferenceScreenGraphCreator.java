package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;

@FunctionalInterface
public interface SearchablePreferenceScreenGraphCreator<C> {

    SearchablePreferenceScreenGraph createGraph(// FK-FIXME: für actualConfiguration wird manchmal null übergeben, was bei mir verboten ist.
                                                C actualConfiguration,
                                                FragmentActivity activityContext);
}
