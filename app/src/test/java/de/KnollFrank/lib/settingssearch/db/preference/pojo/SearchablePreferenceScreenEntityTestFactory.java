package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.common.graph.ValueGraphs;

public class SearchablePreferenceScreenEntityTestFactory {

    public record SearchablePreferenceScreenEntityAndDbDataProvider(
            SearchablePreferenceScreenEntity entity,
            DbDataProvider dbDataProvider) {
    }

    public static SearchablePreferenceScreenEntityAndDbDataProvider createSomeSearchablePreferenceScreen(final SearchablePreferenceScreenGraphTestFactory.Data data) {
        final GraphAndDbDataProvider entityGraphAndDbDataProvider =
                SearchablePreferenceScreenGraphTestFactory
                        .createGraph(
                                PreferenceFragmentCompat.class,
                                Locale.GERMAN,
                                data)
                        .entityGraphAndDbDataProvider();
        return new SearchablePreferenceScreenEntityAndDbDataProvider(
                ValueGraphs
                        .getRootNode(entityGraphAndDbDataProvider.asGraph().graph())
                        .orElseThrow(),
                entityGraphAndDbDataProvider.dbDataProvider());
    }
}
