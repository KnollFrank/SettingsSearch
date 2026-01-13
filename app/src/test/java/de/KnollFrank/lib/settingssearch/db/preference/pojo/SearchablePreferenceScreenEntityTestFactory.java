package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.common.graph.Graphs;

public class SearchablePreferenceScreenEntityTestFactory {

    public record SearchablePreferenceScreenEntityAndDbDataProvider(
            SearchablePreferenceScreenEntity entity,
            DbDataProvider dbDataProvider) {
    }

    public static SearchablePreferenceScreenEntityAndDbDataProvider createSomeSearchablePreferenceScreen(final SearchablePreferenceScreenGraphTestFactory.Data data) {
        final TreeAndDbDataProvider entityTreeAndDbDataProvider =
                SearchablePreferenceScreenGraphTestFactory
                        .createGraph(
                                PreferenceFragmentCompat.class,
                                Locale.GERMAN,
                                data)
                        .entityTreeAndDbDataProvider();
        return new SearchablePreferenceScreenEntityAndDbDataProvider(
                Graphs
                        .getRootNode(entityTreeAndDbDataProvider.asGraph().graph())
                        .orElseThrow(),
                entityTreeAndDbDataProvider.dbDataProvider());
    }
}
