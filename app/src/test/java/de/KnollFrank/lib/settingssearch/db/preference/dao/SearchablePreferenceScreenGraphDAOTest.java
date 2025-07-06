package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.Iterables;

import org.jgrapht.Graph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Locale;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory;
import de.KnollFrank.lib.settingssearch.graph.EntityGraphPojoGraphConverter;
import de.KnollFrank.lib.settingssearch.graph.GraphForLocale;
import de.KnollFrank.lib.settingssearch.graph.PojoGraphEquality;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceScreenGraphDAOTest extends AppDatabaseTest {

    @Test
    public void shouldPersistSearchablePreferenceScreenGraph() {
        // Given
        final SearchablePreferenceScreenGraphDAO dao =
                new SearchablePreferenceScreenGraphDAO(
                        new EntityGraphPojoGraphConverter(),
                        appDatabase.searchablePreferenceScreenGraphEntityDAO());
        final GraphForLocale graphForLocale =
                new GraphForLocale(
                        SearchablePreferenceScreenGraphTestFactory
                                .createGraph(PreferenceFragmentCompat.class)
                                .pojoGraph(),
                        Locale.GERMAN);

        // When
        dao.persist(graphForLocale);
        final GraphForLocale graphForLocaleFromDb = dao.load().orElseThrow();

        // Then
        assertActualEqualsExpected(graphForLocaleFromDb, graphForLocale);
    }

    @Test
    public void shouldOverwritePersistedSearchablePreferenceScreenGraph() {
        // Given
        final SearchablePreferenceScreenGraphDAO dao =
                new SearchablePreferenceScreenGraphDAO(
                        new EntityGraphPojoGraphConverter(),
                        appDatabase.searchablePreferenceScreenGraphEntityDAO());

        // When
        dao.persist(
                new GraphForLocale(
                        SearchablePreferenceScreenGraphTestFactory
                                .createSingleNodeGraph(PreferenceFragmentCompat.class)
                                .pojoGraph(),
                        Locale.GERMAN));

        // And
        final GraphForLocale graphForLocale =
                new GraphForLocale(
                        SearchablePreferenceScreenGraphTestFactory
                                .createGraph(PreferenceFragmentCompat.class)
                                .pojoGraph(),
                        Locale.CHINESE);
        dao.persist(graphForLocale);

        // Then
        final GraphForLocale graphForLocaleFromDb = dao.load().orElseThrow();
        assertActualEqualsExpected(graphForLocaleFromDb, graphForLocale);
    }

    @Test
    public void test_persistTwice_checkAllPreferences() {
        // Given
        final SearchablePreferenceScreenGraphDAO dao =
                new SearchablePreferenceScreenGraphDAO(
                        new EntityGraphPojoGraphConverter(),
                        appDatabase.searchablePreferenceScreenGraphEntityDAO());
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph =
                SearchablePreferenceScreenGraphTestFactory
                        .createSingleNodeGraph(PreferenceFragmentCompat.class)
                        .pojoGraph();
        final Set<SearchablePreference> allPreferences = getAllPreferencesOfSingleNode(graph);

        // When
        dao.persist(new GraphForLocale(graph, Locale.GERMAN));
        dao.persist(dao.load().orElseThrow());

        // Then
        final Set<SearchablePreference> allPreferencesFromDb = getAllPreferencesOfSingleNode(dao.load().orElseThrow().graph());
        assertThat(allPreferencesFromDb, is(allPreferences));
    }

    private static Set<SearchablePreference> getAllPreferencesOfSingleNode(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph) {
        return Iterables
                .getOnlyElement(graph.vertexSet())
                .allPreferences();
    }

    private static void assertActualEqualsExpected(final GraphForLocale actual, final GraphForLocale expected) {
        PojoGraphEquality.assertActualEqualsExpected(actual.graph(), expected.graph());
        assertThat(actual.locale(), is(expected.locale()));
    }
}
