package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.Graphs;

import androidx.preference.PreferenceFragmentCompat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Locale;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory;
import de.KnollFrank.lib.settingssearch.graph.EntityGraphPojoGraphConverter;
import de.KnollFrank.lib.settingssearch.graph.PojoGraphEquality;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceScreenGraphDAOTest extends AppDatabaseTest {

    @Test
    public void shouldNotFindGraphById_emptyDb() {
        // Given
        final SearchablePreferenceScreenGraphDAO dao = createGraphDAO();

        // When
        final Optional<SearchablePreferenceScreenGraph> graphFromDb = dao.findGraphById(Locale.GERMAN);

        // Then
        assertThat(graphFromDb, is(Optional.empty()));
    }

    @Test
    public void test_persist_findGraphById() {
        // Given
        final SearchablePreferenceScreenGraphDAO dao = createGraphDAO();
        final SearchablePreferenceScreenGraph searchablePreferenceScreenGraph =
                new SearchablePreferenceScreenGraph(
                        SearchablePreferenceScreenGraphTestFactory
                                .createGraph(
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
                                                "graph-screen2"))
                                .pojoGraph(),
                        Locale.GERMAN);

        // When
        dao.persist(searchablePreferenceScreenGraph);
        testFindGraphById(searchablePreferenceScreenGraph, dao);
    }

    @Test
    public void test_persistAndFindGraphById_twoGraphs_differentLocales() {
        // Given
        final SearchablePreferenceScreenGraphDAO dao = createGraphDAO();

        // When
        final SearchablePreferenceScreenGraph germanGraph =
                asSearchablePreferenceScreenGraph(
                        SearchablePreferenceScreenGraphTestFactory.createSingleNodeGraph(
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
                                        "graph-screen2")));
        dao.persist(germanGraph);

        // And
        final SearchablePreferenceScreenGraph chineseGraph =
                asSearchablePreferenceScreenGraph(
                        SearchablePreferenceScreenGraphTestFactory.createGraph(
                                PreferenceFragmentCompat.class,
                                Locale.CHINESE,
                                new SearchablePreferenceScreenGraphTestFactory.Data(
                                        "50",
                                        "40",
                                        "parentKey2",
                                        "10",
                                        "20",
                                        "30",
                                        "zh-singleNodeGraph-screen1",
                                        "zh-graph-screen1",
                                        "zh-graph-screen2")));
        dao.persist(chineseGraph);

        // Then
        testFindGraphById(germanGraph, dao);
        testFindGraphById(chineseGraph, dao);
    }

    @Test
    public void test_persistAndFindGraphById_twoGraphs_sameLocales() {
        // Given
        final SearchablePreferenceScreenGraphDAO dao = createGraphDAO();
        final Locale locale = Locale.GERMAN;

        // When
        final SearchablePreferenceScreenGraph graphToBeOverwritten =
                asSearchablePreferenceScreenGraph(
                        SearchablePreferenceScreenGraphTestFactory
                                .createSingleNodeGraph(
                                        PreferenceFragmentCompat.class,
                                        locale,
                                        new SearchablePreferenceScreenGraphTestFactory.Data(
                                                "5",
                                                "4",
                                                "parentKey",
                                                "1",
                                                "2",
                                                "3",
                                                "singleNodeGraph-screen1",
                                                "graph-screen1",
                                                "graph-screen2")));
        dao.persist(graphToBeOverwritten);

        // And
        final SearchablePreferenceScreenGraph overwritingGraph =
                asSearchablePreferenceScreenGraph(
                        SearchablePreferenceScreenGraphTestFactory
                                .createGraph(
                                        PreferenceFragmentCompat.class,
                                        locale,
                                        new SearchablePreferenceScreenGraphTestFactory.Data(
                                                "50",
                                                "40",
                                                "parentKey2",
                                                "10",
                                                "20",
                                                "30",
                                                "singleNodeGraph-screen1",
                                                "graph-screen1",
                                                "graph-screen2")));
        dao.persist(overwritingGraph);

        // Then
        testFindGraphById(overwritingGraph, dao);
    }

    private SearchablePreferenceScreenGraphDAO createGraphDAO() {
        return new SearchablePreferenceScreenGraphDAO(
                new EntityGraphPojoGraphConverter(),
                appDatabase.searchablePreferenceScreenGraphEntityDAO());
    }

    private static void testFindGraphById(final SearchablePreferenceScreenGraph searchablePreferenceScreenGraph,
                                          final SearchablePreferenceScreenGraphDAO dao) {
        final SearchablePreferenceScreenGraph searchablePreferenceScreenGraphFromDb =
                dao
                        .findGraphById(searchablePreferenceScreenGraph.locale())
                        .orElseThrow();
        assertActualEqualsExpected(searchablePreferenceScreenGraphFromDb, searchablePreferenceScreenGraph);
    }

    private static void assertActualEqualsExpected(final SearchablePreferenceScreenGraph actual, final SearchablePreferenceScreenGraph expected) {
        PojoGraphEquality.assertActualEqualsExpected(actual.graph(), expected.graph());
        assertThat(actual.locale(), is(expected.locale()));
    }

    private static SearchablePreferenceScreenGraph asSearchablePreferenceScreenGraph(final Graphs graphs) {
        return new SearchablePreferenceScreenGraph(
                graphs.pojoGraph(),
                graphs.entityGraphAndDbDataProvider().graph().id());
    }
}
