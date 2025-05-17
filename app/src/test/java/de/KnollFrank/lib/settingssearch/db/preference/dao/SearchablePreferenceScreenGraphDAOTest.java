package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.createPojoGraph;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.createSingleNodePojoGraph;
import static de.KnollFrank.lib.settingssearch.test.SearchablePreferenceScreenGraphEquality.assertActualEqualsExpected;

import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceScreenGraphDAOTest extends AppDatabaseTest {

    @Test
    public void shouldPersistSearchablePreferenceScreenGraph() {
        // Given
        final SearchablePreferenceScreenGraphDAO dao = new SearchablePreferenceScreenGraphDAO(appDatabase.searchablePreferenceScreenDAO());
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph = createPojoGraph(PreferenceFragmentCompat.class);

        // When
        dao.persist(graph);
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graphFromDb = dao.load();

        // Then
        assertActualEqualsExpected(graphFromDb, graph);
    }

    @Test
    public void shouldOverwritePersistedSearchablePreferenceScreenGraph() {
        // Given
        final SearchablePreferenceScreenGraphDAO dao = new SearchablePreferenceScreenGraphDAO(appDatabase.searchablePreferenceScreenDAO());

        // When
        dao.persist(createSingleNodePojoGraph(PreferenceFragmentCompat.class));

        // And
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph = createPojoGraph(PreferenceFragmentCompat.class);
        dao.persist(graph);

        // Then
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graphFromDb = dao.load();
        assertActualEqualsExpected(graphFromDb, graph);
    }
}
