package de.KnollFrank.lib.settingssearch.db.preference.db.transformer;

import androidx.fragment.app.FragmentActivity;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;

@FunctionalInterface
public interface SearchablePreferenceScreenTreeTransformer<C> {

    SearchablePreferenceScreenTree transformTree(SearchablePreferenceScreenTree tree,
                                                 // FK-FIXME: für actualConfiguration wird manchmal null übergeben, was bei mir verboten ist.
                                                 C actualConfiguration,
                                                 FragmentActivity activityContext);
}
