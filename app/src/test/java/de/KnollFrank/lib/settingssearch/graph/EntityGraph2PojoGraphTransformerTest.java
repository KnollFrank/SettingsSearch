package de.KnollFrank.lib.settingssearch.graph;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.createGraph;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.createSingleNodeGraph;

import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;
import org.junit.Test;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.GraphAndDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.Graphs;

public class EntityGraph2PojoGraphTransformerTest {

    @Test
    public void test_toPojoGraph_singleNodeGraph() {
        final Graphs graphs =
                createSingleNodeGraph(
                        PreferenceFragmentCompat.class,
                        Locale.GERMAN,
                        new SearchablePreferenceScreenGraphTestFactory.Data(
                                "5",
                                "4",
                                "parentKey",
                                "1",
                                "2",
                                "3",
                                "singleNodeGraph-screen1",
                                "graph-screen1",
                                "graph-screen2"));
        test_toPojoGraph(
                graphs.entityGraphAndDbDataProvider(),
                graphs.pojoGraph());
    }

    @Test
    public void test_toPojoGraph_multiNodeGraph() {
        final Graphs graphs =
                createGraph(
                        PreferenceFragmentCompat.class,
                        Locale.GERMAN,
                        new SearchablePreferenceScreenGraphTestFactory.Data(
                                "5",
                                "4",
                                "parentKey",
                                "1",
                                "2",
                                "3",
                                "singleNodeGraph-screen1",
                                "graph-screen1",
                                "graph-screen2"));
        test_toPojoGraph(
                graphs.entityGraphAndDbDataProvider(),
                graphs.pojoGraph());
    }

    private static void test_toPojoGraph(final GraphAndDbDataProvider entityGraphAndDbDataProvider,
                                         final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraphExpected) {
        // When
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraphActual =
                EntityGraph2PojoGraphTransformer.toPojoGraph(
                        entityGraphAndDbDataProvider.asGraph(),
                        entityGraphAndDbDataProvider.dbDataProvider());

        // Then
        PojoGraphEquality.assertActualEqualsExpected(pojoGraphActual, pojoGraphExpected);
    }
}