package de.KnollFrank.lib.settingssearch.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.createGraph;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.createSingleNodeGraph;

import androidx.preference.PreferenceFragmentCompat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.GraphAndDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.Graphs;

@RunWith(RobolectricTestRunner.class)
public class EntityGraphToPojoGraphTransformerTest {

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
                graphs.pojoTree());
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
                graphs.pojoTree());
    }

    private static void test_toPojoGraph(final GraphAndDbDataProvider entityGraphAndDbDataProvider,
                                         final Tree<SearchablePreferenceScreen, SearchablePreference> pojoTreeExpected) {
        // When
        final Tree<SearchablePreferenceScreen, SearchablePreference> pojoGraphActual =
                EntityGraphToPojoGraphTransformer.toPojoGraph(
                        entityGraphAndDbDataProvider.asGraph(),
                        entityGraphAndDbDataProvider.dbDataProvider());

        // Then
        assertThat(pojoGraphActual, is(pojoTreeExpected));
    }
}