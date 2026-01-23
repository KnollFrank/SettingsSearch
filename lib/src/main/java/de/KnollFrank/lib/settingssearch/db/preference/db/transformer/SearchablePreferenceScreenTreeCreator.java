package de.KnollFrank.lib.settingssearch.db.preference.db.transformer;

import androidx.fragment.app.FragmentActivity;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;

@FunctionalInterface
public interface SearchablePreferenceScreenTreeCreator<C> {

    // FK-TODO: Parameter von createTree() optimieren
    SearchablePreferenceScreenTree createTree(Locale locale,
                                              // FK-FIXME: für targetConfiguration wird manchmal null übergeben, was bei mir verboten ist.
                                              C targetConfiguration,
                                              FragmentActivity activityContext);
}
