package de.KnollFrank.lib.settingssearch.db.preference.db.transformer;

import androidx.fragment.app.FragmentActivity;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;

@FunctionalInterface
public interface SearchablePreferenceScreenGraphCreator<C> {

    SearchablePreferenceScreenGraph createGraph(Locale locale,
                                                // FK-FIXME: für actualConfiguration wird manchmal null übergeben, was bei mir verboten ist.
                                                C actualConfiguration,
                                                FragmentActivity activityContext);
}
