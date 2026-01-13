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

import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesRoomDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.PersistableBundleTestFactory;
import de.KnollFrank.lib.settingssearch.graph.EntityTreePojoTreeConverter;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceScreenTreeDAOTest extends PreferencesRoomDatabaseTest {

    @Test
    public void shouldNotFindTreeById_emptyDb() {
        // Given
        final SearchablePreferenceScreenTreeDAO dao = createGraphDAO();

        // When
        final Optional<SearchablePreferenceScreenTree> graphFromDb = dao.findTreeById(Locale.GERMAN);

        // Then
        assertThat(graphFromDb, is(Optional.empty()));
    }

    @Test
    public void test_persistOrReplace_findTreeById() {
        // Given
        final SearchablePreferenceScreenTreeDAO dao = createGraphDAO();
        final SearchablePreferenceScreenTree searchablePreferenceScreenTree =
                new SearchablePreferenceScreenTree(
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
                                                "tree-screen1",
                                                "tree-screen2"))
                                .pojoTree(),
                        Locale.GERMAN,
                        PersistableBundleTestFactory.createSomePersistableBundle());

        // When
        dao.persistOrReplace(searchablePreferenceScreenTree);
        testFindTreeById(searchablePreferenceScreenTree, dao);
    }

    @Test
    public void test_persistOrReplaceAndFindTreeById_twoGraphs_differentLocales() {
        // Given
        final SearchablePreferenceScreenTreeDAO dao = createGraphDAO();

        // When
        final SearchablePreferenceScreenTree germanGraph =
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
                                        "tree-screen1",
                                        "tree-screen2")));
        dao.persistOrReplace(germanGraph);

        // And
        final SearchablePreferenceScreenTree chineseGraph =
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
                                        "zh-tree-screen1",
                                        "zh-tree-screen2")));
        dao.persistOrReplace(chineseGraph);

        // Then
        testFindTreeById(germanGraph, dao);
        testFindTreeById(chineseGraph, dao);
    }

    @Test
    public void test_persistOrReplaceAndFindTreeById_twoGraphs_sameLocales() {
        // Given
        final SearchablePreferenceScreenTreeDAO dao = createGraphDAO();
        final Locale locale = Locale.GERMAN;

        // When
        final SearchablePreferenceScreenTree graphToBeOverwritten =
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
                                                "tree-screen1",
                                                "tree-screen2")));
        dao.persistOrReplace(graphToBeOverwritten);

        // And
        final SearchablePreferenceScreenTree overwritingGraph =
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
                                                "tree-screen1",
                                                "tree-screen2")));
        dao.persistOrReplace(overwritingGraph);

        // Then
        testFindTreeById(overwritingGraph, dao);
    }

    private SearchablePreferenceScreenTreeDAO createGraphDAO() {
        return new SearchablePreferenceScreenTreeDAO(
                new EntityTreePojoTreeConverter(),
                preferencesRoomDatabase.searchablePreferenceScreenGraphEntityDAO());
    }

    private static void testFindTreeById(final SearchablePreferenceScreenTree searchablePreferenceScreenTree,
                                         final SearchablePreferenceScreenTreeDAO dao) {
        final SearchablePreferenceScreenTree searchablePreferenceScreenTreeFromDb =
                dao
                        .findTreeById(searchablePreferenceScreenTree.locale())
                        .orElseThrow();
        assertActualEqualsExpected(searchablePreferenceScreenTreeFromDb, searchablePreferenceScreenTree);
    }

    private static void assertActualEqualsExpected(final SearchablePreferenceScreenTree actual, final SearchablePreferenceScreenTree expected) {
        assertThat(actual.tree().graph(), is(expected.tree().graph()));
        assertThat(actual.locale(), is(expected.locale()));
    }

    private static SearchablePreferenceScreenTree asSearchablePreferenceScreenGraph(final Graphs graphs) {
        return new SearchablePreferenceScreenTree(
                graphs.pojoTree(),
                graphs.entityGraphAndDbDataProvider().graph().id(),
                graphs.entityGraphAndDbDataProvider().graph().configuration());
    }
}
