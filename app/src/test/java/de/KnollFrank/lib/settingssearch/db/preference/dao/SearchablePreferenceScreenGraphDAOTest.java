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
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory;
import de.KnollFrank.lib.settingssearch.graph.EntityGraphPojoGraphConverter;
import de.KnollFrank.lib.settingssearch.graph.GraphForLocale;
import de.KnollFrank.lib.settingssearch.graph.PojoGraphEquality;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceScreenGraphDAOTest extends AppDatabaseTest {

    @Test
    public void shouldNotFindGraphById_emptyDb() {
        // Given
        final SearchablePreferenceScreenGraphDAO dao = createGraphDAO();

        // When
        final Optional<GraphForLocale> graphFromDb = dao.findGraphById(Locale.GERMAN);

        // Then
        assertThat(graphFromDb, is(Optional.empty()));
    }

    @Test
    public void test_persist_findGraphById() {
        // Given
        final SearchablePreferenceScreenGraphDAO dao = createGraphDAO();
        final GraphForLocale graphForLocale =
                new GraphForLocale(
                        SearchablePreferenceScreenGraphTestFactory
                                .createGraph(
                                        PreferenceFragmentCompat.class,
                                        Locale.GERMAN,
                                        new SearchablePreferenceScreenGraphTestFactory.Data(
                                                5,
                                                4,
                                                "parentKey",
                                                1,
                                                2,
                                                3,
                                                "singleNodeGraph-screen1",
                                                "graph-screen1",
                                                "graph-screen2"))
                                .pojoGraph(),
                        Locale.GERMAN);

        // When
        dao.persist(graphForLocale);
        testFindGraphById(graphForLocale, dao);
    }

    @Test
    public void test_persistAndFindGraphById_twoGraphs_differentLocales() {
        // Given
        final SearchablePreferenceScreenGraphDAO dao = createGraphDAO();

        // When
        final GraphForLocale germanGraph =
                asGraphForLocale(
                        SearchablePreferenceScreenGraphTestFactory.createSingleNodeGraph(
                                PreferenceFragmentCompat.class,
                                Locale.GERMAN,
                                new SearchablePreferenceScreenGraphTestFactory.Data(
                                        5,
                                        4,
                                        "parentKey",
                                        1,
                                        2,
                                        3,
                                        "singleNodeGraph-screen1",
                                        "graph-screen1",
                                        "graph-screen2")));
        dao.persist(germanGraph);

        // And
        final GraphForLocale chineseGraph =
                asGraphForLocale(
                        SearchablePreferenceScreenGraphTestFactory.createGraph(
                                PreferenceFragmentCompat.class,
                                Locale.CHINESE,
                                new SearchablePreferenceScreenGraphTestFactory.Data(
                                        50,
                                        40,
                                        "parentKey2",
                                        10,
                                        20,
                                        30,
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
        final GraphForLocale graphToBeOverwritten =
                asGraphForLocale(
                        SearchablePreferenceScreenGraphTestFactory
                                .createSingleNodeGraph(
                                        PreferenceFragmentCompat.class,
                                        locale,
                                        new SearchablePreferenceScreenGraphTestFactory.Data(
                                                5,
                                                4,
                                                "parentKey",
                                                1,
                                                2,
                                                3,
                                                "singleNodeGraph-screen1",
                                                "graph-screen1",
                                                "graph-screen2")));
        dao.persist(graphToBeOverwritten);

        // And
        final GraphForLocale overwritingGraph =
                asGraphForLocale(
                        SearchablePreferenceScreenGraphTestFactory
                                .createGraph(
                                        PreferenceFragmentCompat.class,
                                        locale,
                                        new SearchablePreferenceScreenGraphTestFactory.Data(
                                                50,
                                                40,
                                                "parentKey2",
                                                10,
                                                20,
                                                30,
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

    private static void testFindGraphById(final GraphForLocale graphForLocale,
                                          final SearchablePreferenceScreenGraphDAO dao) {
        final GraphForLocale graphForLocaleFromDb =
                dao
                        .findGraphById(graphForLocale.locale())
                        .orElseThrow();
        assertActualEqualsExpected(graphForLocaleFromDb, graphForLocale);
    }

    private static void assertActualEqualsExpected(final GraphForLocale actual, final GraphForLocale expected) {
        PojoGraphEquality.assertActualEqualsExpected(actual.graph(), expected.graph());
        assertThat(actual.locale(), is(expected.locale()));
    }

    private static GraphForLocale asGraphForLocale(final Graphs graphs) {
        return new GraphForLocale(
                graphs.pojoGraph(),
                graphs.entityGraphAndDbDataProvider().graph().id());
    }
}
