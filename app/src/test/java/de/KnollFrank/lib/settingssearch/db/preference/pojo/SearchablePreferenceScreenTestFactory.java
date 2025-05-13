package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.createPojoGraph;

import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.lib.settingssearch.common.graph.GraphUtils;

public class SearchablePreferenceScreenTestFactory {

    public static SearchablePreferenceScreen createSomeSearchablePreferenceScreen() {
        return GraphUtils.getRootNode(createPojoGraph(PreferenceFragmentCompat.class));
    }
}
