package de.KnollFrank.lib.settingssearch.graph;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.createGraph;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.createSingleNodeGraph;

import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;
import org.junit.Test;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.Graphs;

public class EntityGraph2PojoGraphTransformerTest {

    @Test
    public void test_toPojoGraph_singleNodeGraph() {
        final Graphs graphs = createSingleNodeGraph(PreferenceFragmentCompat.class);
        test_toPojoGraph(
                graphs.entityGraphAndDbDataProvider(),
                graphs.pojoGraph());
    }

    @Test
    public void test_toPojoGraph_multiNodeGraph() {
        final Graphs graphs = createGraph(PreferenceFragmentCompat.class);
        test_toPojoGraph(
                graphs.entityGraphAndDbDataProvider(),
                graphs.pojoGraph());
    }

    private static void test_toPojoGraph(final GraphAndDbDataProvider entityGraphAndDbDataProvider,
                                         final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraphExpected) {
        // When
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraphActual =
                EntityGraph2PojoGraphTransformer.toPojoGraph(
                        entityGraphAndDbDataProvider
                                .graph()
                                .asGraph(entityGraphAndDbDataProvider.dbDataProvider()),
                        entityGraphAndDbDataProvider.dbDataProvider());

        // Then
        PojoGraphEquality.assertActualEqualsExpected(pojoGraphActual, pojoGraphExpected);
    }
}