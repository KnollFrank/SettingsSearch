package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.test.GraphEquality.assertActualEqualsExpected;

import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.Iterables;

import org.jgrapht.Graph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity.DbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceScreenGraphDAOTest extends AppDatabaseTest {

    @Test
    public void shouldPersistSearchablePreferenceScreenGraph() {
        // Given
        final SearchablePreferenceScreenGraphDAO dao = new SearchablePreferenceScreenGraphDAO(appDatabase.searchablePreferenceScreenEntityDAO(), appDatabase.searchablePreferenceEntityDAO());
        final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> graph =
                SearchablePreferenceScreenGraphTestFactory
                        .createGraph(PreferenceFragmentCompat.class)
                        .entityGraphAndDetachedDbDataProvider()
                        .entityGraph();

        // When
        dao.persist(graph);
        final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> graphFromDb = dao.load();

        // Then
        assertActualEqualsExpected(graphFromDb, graph);
    }

    @Test
    public void shouldOverwritePersistedSearchablePreferenceScreenGraph() {
        // Given
        final SearchablePreferenceScreenGraphDAO dao = new SearchablePreferenceScreenGraphDAO(appDatabase.searchablePreferenceScreenEntityDAO(), appDatabase.searchablePreferenceEntityDAO());

        // When
        dao.persist(
                SearchablePreferenceScreenGraphTestFactory
                        .createSingleNodeGraph(PreferenceFragmentCompat.class)
                        .entityGraphAndDetachedDbDataProvider()
                        .entityGraph());

        // And
        final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> graph =
                SearchablePreferenceScreenGraphTestFactory
                        .createGraph(PreferenceFragmentCompat.class)
                        .entityGraphAndDetachedDbDataProvider()
                        .entityGraph();
        dao.persist(graph);

        // Then
        final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> graphFromDb = dao.load();
        assertActualEqualsExpected(graphFromDb, graph);
    }

    @Test
    public void test_persistTwice_checkAllPreferences() {
        // Given
        final SearchablePreferenceScreenGraphDAO dao =
                new SearchablePreferenceScreenGraphDAO(
                        appDatabase.searchablePreferenceScreenEntityDAO(),
                        appDatabase.searchablePreferenceEntityDAO());
        final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> graph =
                SearchablePreferenceScreenGraphTestFactory
                        .createSingleNodeGraph(PreferenceFragmentCompat.class)
                        .entityGraphAndDetachedDbDataProvider()
                        .entityGraph();
        final Set<SearchablePreferenceEntity> allPreferences =
                getAllPreferencesOfSingleNode(
                        graph,
                        appDatabase.searchablePreferenceScreenEntityDAO());

        // When
        dao.persist(graph);
        dao.persist(dao.load());

        // Then
        final Set<SearchablePreferenceEntity> allPreferencesFromDb =
                getAllPreferencesOfSingleNode(
                        dao.load(),
                        appDatabase.searchablePreferenceScreenEntityDAO());
        assertThat(allPreferencesFromDb, is(allPreferences));
    }

    private static Set<SearchablePreferenceEntity> getAllPreferencesOfSingleNode(final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> graph,
                                                                                 final DbDataProvider dbDataProvider) {
        return Iterables
                .getOnlyElement(graph.vertexSet())
                .getAllPreferences(dbDataProvider);
    }
}
