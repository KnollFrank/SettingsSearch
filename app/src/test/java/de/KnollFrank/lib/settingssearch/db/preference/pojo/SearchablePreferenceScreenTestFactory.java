package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.lib.settingssearch.common.graph.GraphUtils;
import de.KnollFrank.lib.settingssearch.graph.EntityGraphAndDbDataProvider;

public class SearchablePreferenceScreenTestFactory {

    public record SearchablePreferenceScreenEntityAndDbDataProvider(
            SearchablePreferenceScreenEntity entity, DbDataProvider dbDataProvider) {
    }

    public static SearchablePreferenceScreenEntityAndDbDataProvider createSomeSearchablePreferenceScreen() {
        final EntityGraphAndDbDataProvider entityGraphAndDbDataProvider =
                SearchablePreferenceScreenGraphTestFactory
                        .createGraph(PreferenceFragmentCompat.class)
                        .entityGraphAndDbDataProvider();
        return new SearchablePreferenceScreenEntityAndDbDataProvider(
                GraphUtils
                        .getRootNode(entityGraphAndDbDataProvider.entityGraph())
                        .orElseThrow(),
                entityGraphAndDbDataProvider.dbDataProvider());
    }
}
