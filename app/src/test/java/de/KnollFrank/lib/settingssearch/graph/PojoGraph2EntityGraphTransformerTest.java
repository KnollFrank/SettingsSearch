package de.KnollFrank.lib.settingssearch.graph;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.createGraph;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.createSingleNodeGraph;

import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.EntityGraphAndDetachedDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.Graphs;

@RunWith(RobolectricTestRunner.class)
public class PojoGraph2EntityGraphTransformerTest {

    @Test
    public void test_toPojoGraph_singleNodeGraph() {
        final Graphs graphs = createSingleNodeGraph(PreferenceFragmentCompat.class);
        test_toEntityGraph(
                graphs.pojoGraph(),
                getEntityGraphAndDbDataProviders(graphs.entityGraphAndDetachedDbDataProvider()));
    }

    @Test
    public void test_toPojoGraph_multiNodeGraph() {
        final Graphs graphs = createGraph(PreferenceFragmentCompat.class);
        test_toEntityGraph(
                graphs.pojoGraph(),
                getEntityGraphAndDbDataProviders(graphs.entityGraphAndDetachedDbDataProvider()));
    }

    private static void test_toEntityGraph(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraph,
                                           final EntityGraphAndDbDataProvider entityGraphAndDbDataProviderExpected) {
        // When
        final EntityGraphAndDbDataProvider entityGraphAndDbDataProviderActual =
                PojoGraph2EntityGraphTransformer.toEntityGraph(pojoGraph);

        // Then
        EntityGraphEquality.assertActualEqualsExpected(
                entityGraphAndDbDataProviderActual,
                entityGraphAndDbDataProviderExpected);
    }

    private static EntityGraphAndDbDataProvider getEntityGraphAndDbDataProviders(final EntityGraphAndDetachedDbDataProvider entityGraphAndDetachedDbDataProvider) {
        return new EntityGraphAndDbDataProvider(
                entityGraphAndDetachedDbDataProvider.entityGraph(),
                entityGraphAndDetachedDbDataProvider.detachedDbDataProvider());
    }
}