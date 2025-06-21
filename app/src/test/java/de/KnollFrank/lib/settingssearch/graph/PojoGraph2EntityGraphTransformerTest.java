package de.KnollFrank.lib.settingssearch.graph;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.createGraph;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.createSingleNodeGraph;

import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;
import org.junit.Test;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviders;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.EntityGraphAndDetachedDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.Graphs;

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
                                           final EntityGraphAndDbDataProviders entityGraphAndDbDataProvidersExpected) {
        // When
        final EntityGraphAndDbDataProviders entityGraphAndDbDataProvidersActual =
                PojoGraph2EntityGraphTransformer.toEntityGraph(pojoGraph);

        // Then
        EntityGraphEquality.assertActualEqualsExpected(
                entityGraphAndDbDataProvidersActual,
                entityGraphAndDbDataProvidersExpected);
    }

    private static EntityGraphAndDbDataProviders getEntityGraphAndDbDataProviders(final EntityGraphAndDetachedDbDataProvider entityGraphAndDetachedDbDataProvider) {
        return new EntityGraphAndDbDataProviders(
                entityGraphAndDetachedDbDataProvider.entityGraph(),
                new DbDataProviders(
                        entityGraphAndDetachedDbDataProvider.detachedDbDataProvider(),
                        entityGraphAndDetachedDbDataProvider.detachedDbDataProvider()));
    }
}