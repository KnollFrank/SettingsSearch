package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.lib.settingssearch.common.Pair;
import de.KnollFrank.lib.settingssearch.common.graph.GraphUtils;
import de.KnollFrank.lib.settingssearch.graph.EntityGraphAndDbDataProvider;

public class SearchablePreferenceScreenTestFactory {

    public static Pair<SearchablePreferenceScreenEntity, DbDataProvider> createSomeSearchablePreferenceScreen() {
        final EntityGraphAndDbDataProvider entityGraphAndDbDataProvider =
                SearchablePreferenceScreenGraphTestFactory
                        .createGraph(PreferenceFragmentCompat.class)
                        .entityGraphAndDbDataProvider();
        return Pair.create(
                GraphUtils
                        .getRootNode(entityGraphAndDbDataProvider.entityGraph())
                        .orElseThrow(),
                entityGraphAndDbDataProvider.dbDataProvider());
    }
}
