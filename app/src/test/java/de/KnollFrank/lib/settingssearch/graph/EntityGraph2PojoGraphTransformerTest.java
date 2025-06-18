package de.KnollFrank.lib.settingssearch.graph;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.createPojoGraph;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.createSingleNodePojoGraph;

import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;
import org.junit.Test;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviders;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.EntityGraphAndDetachedDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.Graphs;

public class EntityGraph2PojoGraphTransformerTest {

    @Test
    public void test_toPojoGraph_singleNodeGraph() {
        final Graphs graphs = createSingleNodePojoGraph(PreferenceFragmentCompat.class);
        test_toPojoGraph(
                graphs.entityGraphAndDetachedDbDataProvider(),
                graphs.pojoGraph());
    }

    @Test
    public void test_toPojoGraph_multiNodeGraph() {
        final Graphs graphs = createPojoGraph(PreferenceFragmentCompat.class);
        test_toPojoGraph(
                graphs.entityGraphAndDetachedDbDataProvider(),
                graphs.pojoGraph());
    }

    private static void test_toPojoGraph(final EntityGraphAndDetachedDbDataProvider entityGraphAndDetachedDbDataProvider,
                                         final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraphExpected) {
        // When
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraphActual =
                EntityGraph2PojoGraphTransformer.toPojoGraph(
                        entityGraphAndDetachedDbDataProvider.entityGraph(),
                        new DbDataProviders(
                                entityGraphAndDetachedDbDataProvider.detachedDbDataProvider(),
                                entityGraphAndDetachedDbDataProvider.detachedDbDataProvider()));

        // Then
        PojoGraphEquality.assertActualEqualsExpected(pojoGraphActual, pojoGraphExpected);
    }
}