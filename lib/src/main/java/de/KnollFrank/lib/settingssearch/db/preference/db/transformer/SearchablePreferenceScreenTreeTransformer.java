package de.KnollFrank.lib.settingssearch.db.preference.db.transformer;

import androidx.fragment.app.FragmentActivity;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;

@FunctionalInterface
public interface SearchablePreferenceScreenTreeTransformer<C> {

    // FK-TODO: Parameter von transformTree() optimieren
    SearchablePreferenceScreenTree<C> transformTree(SearchablePreferenceScreenTree<C> tree,
                                                    // FK-FIXME: für targetConfiguration wird manchmal null übergeben, was bei mir verboten ist.
                                                    C targetConfiguration,
                                                    FragmentActivity activityContext);
}
