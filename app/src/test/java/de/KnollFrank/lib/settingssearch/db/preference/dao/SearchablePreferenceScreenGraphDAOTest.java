package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.Iterables;

import org.jgrapht.Graph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory;
import de.KnollFrank.lib.settingssearch.graph.EntityGraphPojoGraphConverter;
import de.KnollFrank.lib.settingssearch.graph.PojoGraphEquality;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceScreenGraphDAOTest extends AppDatabaseTest {

    @Test
    public void shouldPersistSearchablePreferenceScreenGraph() {
        // Given
        final SearchablePreferenceScreenGraphDAO dao =
                new SearchablePreferenceScreenGraphDAO(
                        new EntityGraphPojoGraphConverter(),
                        new SearchablePreferenceScreenEntityGraphDAO(
                                appDatabase.searchablePreferenceScreenEntityDAO(),
                                appDatabase.searchablePreferenceEntityDAO()));
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph =
                SearchablePreferenceScreenGraphTestFactory
                        .createGraph(PreferenceFragmentCompat.class)
                        .pojoGraph();

        // When
        dao.persist(graph);
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graphFromDb = dao.load();

        // Then
        PojoGraphEquality.assertActualEqualsExpected(graphFromDb, graph);
    }

    @Test
    public void shouldOverwritePersistedSearchablePreferenceScreenGraph() {
        // Given
        final SearchablePreferenceScreenGraphDAO dao =
                new SearchablePreferenceScreenGraphDAO(
                        new EntityGraphPojoGraphConverter(),
                        new SearchablePreferenceScreenEntityGraphDAO(
                                appDatabase.searchablePreferenceScreenEntityDAO(),
                                appDatabase.searchablePreferenceEntityDAO()));

        // When
        dao.persist(
                SearchablePreferenceScreenGraphTestFactory
                        .createSingleNodeGraph(PreferenceFragmentCompat.class)
                        .pojoGraph());

        // And
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph =
                SearchablePreferenceScreenGraphTestFactory
                        .createGraph(PreferenceFragmentCompat.class)
                        .pojoGraph();
        dao.persist(graph);

        // Then
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graphFromDb = dao.load();
        PojoGraphEquality.assertActualEqualsExpected(graphFromDb, graph);
    }

    @Test
    public void test_persistTwice_checkAllPreferences() {
        // Given
        final SearchablePreferenceScreenGraphDAO dao =
                new SearchablePreferenceScreenGraphDAO(
                        new EntityGraphPojoGraphConverter(),
                        new SearchablePreferenceScreenEntityGraphDAO(
                                appDatabase.searchablePreferenceScreenEntityDAO(),
                                appDatabase.searchablePreferenceEntityDAO()));
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph =
                SearchablePreferenceScreenGraphTestFactory
                        .createSingleNodeGraph(PreferenceFragmentCompat.class)
                        .pojoGraph();
        final Set<SearchablePreference> allPreferences = getAllPreferencesOfSingleNode(graph);

        // When
        dao.persist(graph);
        dao.persist(dao.load());

        // Then
        final Set<SearchablePreference> allPreferencesFromDb = getAllPreferencesOfSingleNode(dao.load());
        assertThat(allPreferencesFromDb, is(allPreferences));
    }

    private static Set<SearchablePreference> getAllPreferencesOfSingleNode(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph) {
        return Iterables
                .getOnlyElement(graph.vertexSet())
                .allPreferences();
    }
}
