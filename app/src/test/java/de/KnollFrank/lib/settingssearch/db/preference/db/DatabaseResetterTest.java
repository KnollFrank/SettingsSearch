package de.KnollFrank.lib.settingssearch.db.preference.db;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import androidx.preference.PreferenceFragmentCompat;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory;
import de.KnollFrank.lib.settingssearch.graph.GraphForLocale;

@RunWith(RobolectricTestRunner.class)
public class DatabaseResetterTest {

    @Test
    public void shouldResetDatabase() {
        // Given
        final AppDatabase appDatabase = getAppDatabase();
        initialize(appDatabase);

        // When
        DatabaseResetter.resetDatabase(appDatabase);

        // Then
        assertIsReset(appDatabase);
    }

    private static AppDatabase getAppDatabase() {
        return AppDatabaseFactory.getInstance(ApplicationProvider.getApplicationContext());
    }

    private static void initialize(final AppDatabase appDatabase) {
        final var singleNodeGraph =
                SearchablePreferenceScreenGraphTestFactory.createSingleNodeGraph(
                        PreferenceFragmentCompat.class,
                        Locale.GERMAN,
                        new SearchablePreferenceScreenGraphTestFactory.Data(
                                5,
                                4,
                                "parentKey",
                                10,
                                20,
                                30,
                                "singleNodeGraph-screen1",
                                "graph-screen1",
                                "graph-screen2"));
        appDatabase
                .searchablePreferenceScreenGraphDAO()
                .persist(
                        new GraphForLocale(
                                singleNodeGraph.pojoGraph(),
                                singleNodeGraph.entityGraphAndDbDataProvider().graph().id()));
    }

    private static void assertIsReset(final AppDatabase appDatabase) {
        assertThat(appDatabase.searchablePreferenceScreenGraphDAO().loadAll(), is(empty()));
    }
}
