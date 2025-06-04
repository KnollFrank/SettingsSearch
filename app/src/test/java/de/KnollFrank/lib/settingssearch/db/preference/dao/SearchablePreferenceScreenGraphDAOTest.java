package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.createPojoGraph;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.createSingleNodePojoGraph;
import static de.KnollFrank.lib.settingssearch.test.SearchablePreferenceScreenGraphEquality.assertActualEqualsExpected;

import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.Iterables;

import org.jgrapht.Graph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity.DbDataProvider;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceScreenGraphDAOTest extends AppDatabaseTest {

    @Test
    public void shouldPersistSearchablePreferenceScreenGraph() {
        // Given
        final SearchablePreferenceScreenGraphDAO dao = new SearchablePreferenceScreenGraphDAO(appDatabase.searchablePreferenceScreenDAO(), appDatabase.searchablePreferenceDAO());
        final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEdge> graph = createPojoGraph(PreferenceFragmentCompat.class);

        // When
        dao.persist(graph);
        final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEdge> graphFromDb = dao.load();

        // Then
        assertActualEqualsExpected(graphFromDb, graph);
    }

    @Test
    public void shouldOverwritePersistedSearchablePreferenceScreenGraph() {
        // Given
        final SearchablePreferenceScreenGraphDAO dao = new SearchablePreferenceScreenGraphDAO(appDatabase.searchablePreferenceScreenDAO(), appDatabase.searchablePreferenceDAO());

        // When
        dao.persist(createSingleNodePojoGraph(PreferenceFragmentCompat.class));

        // And
        final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEdge> graph = createPojoGraph(PreferenceFragmentCompat.class);
        dao.persist(graph);

        // Then
        final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEdge> graphFromDb = dao.load();
        assertActualEqualsExpected(graphFromDb, graph);
    }

    @Test
    public void test_persistTwice_checkAllPreferences() {
        // Given
        final SearchablePreferenceScreenGraphDAO dao =
                new SearchablePreferenceScreenGraphDAO(
                        appDatabase.searchablePreferenceScreenDAO(),
                        appDatabase.searchablePreferenceDAO());
        final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEdge> graph = createSingleNodePojoGraph(PreferenceFragmentCompat.class);
        final Set<SearchablePreferenceEntity> allPreferences =
                getAllPreferencesOfSingleNode(
                        graph,
                        appDatabase.searchablePreferenceScreenDAO());

        // When
        dao.persist(graph);
        dao.persist(dao.load());

        // Then
        final Set<SearchablePreferenceEntity> allPreferencesFromDb =
                getAllPreferencesOfSingleNode(
                        dao.load(),
                        appDatabase.searchablePreferenceScreenDAO());
        assertThat(allPreferencesFromDb, is(allPreferences));
    }

    private static Set<SearchablePreferenceEntity> getAllPreferencesOfSingleNode(final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEdge> graph,
                                                                                 final DbDataProvider dbDataProvider) {
        return Iterables
                .getOnlyElement(graph.vertexSet())
                .getAllPreferences(dbDataProvider);
    }
}
