package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.lib.settingssearch.common.graph.GraphUtils;

public class SearchablePreferenceScreenTestFactory {

    public static SearchablePreferenceScreenEntity createSomeSearchablePreferenceScreen() {
        return GraphUtils
                .getRootNode(
                        SearchablePreferenceScreenGraphTestFactory
                                .createGraph(PreferenceFragmentCompat.class)
                                .entityGraphAndDetachedDbDataProvider()
                                .entityGraph())
                .orElseThrow();
    }
}
