package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.graph.Graph2POJOGraphTransformerTest.createPojoGraph;
import static de.KnollFrank.lib.settingssearch.test.SearchablePreferenceScreenEquality.assertActualEqualsExpected;

import androidx.preference.PreferenceFragmentCompat;

import org.jgrapht.Graph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.graph.GraphUtils;
import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceScreenDAOTest extends AppDatabaseTest {

    @Test
    public void shouldPersistAndFindSearchablePreferenceScreenById() {
        // Given
        final SearchablePreferenceScreenDAO dao = appDatabase.searchablePreferenceScreenDAO();
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph = createPojoGraph(PreferenceFragmentCompat.class);
        final SearchablePreferenceScreen screen = GraphUtils.getRootNode(graph);

        // When
        dao.persist(screen);

        // Then the SearchablePreferenceScreen was persisted at all
        final Optional<SearchablePreferenceScreen> screenFromDb = dao.findSearchablePreferenceScreenById(screen.getId());
        assertThat(screenFromDb.isPresent(), is(true));

        // And the SearchablePreferenceScreen was persisted correctly
        assertActualEqualsExpected(screenFromDb.orElseThrow(), screen);
    }
}
