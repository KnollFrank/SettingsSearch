package de.KnollFrank.lib.settingssearch.graph;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.createGraph;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.createSingleNodeGraph;

import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.GraphAndDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.Graphs;

@RunWith(RobolectricTestRunner.class)
public class PojoGraph2EntityGraphTransformerTest {

    @Test
    public void test_toPojoGraph_singleNodeGraph() {
        final Graphs graphs =
                createSingleNodeGraph(
                        PreferenceFragmentCompat.class,
                        Locale.GERMAN,
                        new SearchablePreferenceScreenGraphTestFactory.Data(
                                5,
                                4,
                                "parentKey",
                                1,
                                2,
                                3,
                                "singleNodeGraph-screen1",
                                "graph-screen1",
                                "graph-screen2"));
        test_toEntityGraph(
                graphs.pojoGraph(),
                graphs.entityGraphAndDbDataProvider());
    }

    @Test
    public void test_toPojoGraph_multiNodeGraph() {
        final Graphs graphs =
                createGraph(
                        PreferenceFragmentCompat.class,
                        Locale.GERMAN,
                        new SearchablePreferenceScreenGraphTestFactory.Data(
                                5,
                                4,
                                "parentKey",
                                1,
                                2,
                                3,
                                "singleNodeGraph-screen1",
                                "graph-screen1",
                                "graph-screen2"));
        test_toEntityGraph(
                graphs.pojoGraph(),
                graphs.entityGraphAndDbDataProvider());
    }

    private static void test_toEntityGraph(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraph,
                                           final GraphAndDbDataProvider entityGraphAndDbDataProviderExpected) {
        // When
        final GraphAndDbDataProvider entityGraphAndDbDataProviderActual =
                PojoGraph2EntityGraphTransformer.toEntityGraph(
                        pojoGraph,
                        Locale.GERMAN);

        // Then
        EntityGraphEquality.assertActualEqualsExpected(
                entityGraphAndDbDataProviderActual,
                entityGraphAndDbDataProviderExpected);
    }
}