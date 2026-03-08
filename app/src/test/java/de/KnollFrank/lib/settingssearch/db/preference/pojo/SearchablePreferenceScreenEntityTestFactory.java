package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import static de.KnollFrank.lib.settingssearch.PreferenceFragmentClassOfActivityTestFactory.createSomePreferenceFragmentClassOfActivity;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.common.graph.Graphs;

public class SearchablePreferenceScreenEntityTestFactory {

    public record SearchablePreferenceScreenEntityAndDbDataProvider(
            SearchablePreferenceScreenEntity entity,
            DbDataProvider dbDataProvider) {
    }

    private SearchablePreferenceScreenEntityTestFactory() {
    }

    public static SearchablePreferenceScreenEntityAndDbDataProvider createSomeSearchablePreferenceScreen(final SearchablePreferenceScreenGraphTestFactory.Data data) {
        final TreeAndDbDataProvider entityTreeAndDbDataProvider =
                SearchablePreferenceScreenGraphTestFactory
                        .createGraph(
                                createSomePreferenceFragmentClassOfActivity(),
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
