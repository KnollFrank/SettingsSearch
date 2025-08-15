package de.KnollFrank.lib.settingssearch.db.preference.db;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import androidx.preference.PreferenceFragmentCompat;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.File;
import java.util.Locale;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseConfig.JournalMode;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory;

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
        return AppDatabaseFactory.createAppDatabase(
                new AppDatabaseConfig(
                        "searchable_preferences.db",
                        Optional.of(new File("database/searchable_preferences_prepackaged.db")),
                        JournalMode.AUTOMATIC),
                ApplicationProvider.getApplicationContext());
    }

    private static void initialize(final AppDatabase appDatabase) {
        final var singleNodeGraph =
                SearchablePreferenceScreenGraphTestFactory.createSingleNodeGraph(
                        PreferenceFragmentCompat.class,
                        Locale.GERMAN,
                        new SearchablePreferenceScreenGraphTestFactory.Data(
                                "5",
                                "4",
                                "parentKey",
                                "10",
                                "20",
                                "30",
                                "singleNodeGraph-screen1",
                                "graph-screen1",
                                "graph-screen2"));
        appDatabase
                .searchablePreferenceScreenGraphDAO()
                .persist(
                        new SearchablePreferenceScreenGraph(
                                singleNodeGraph.pojoGraph(),
                                singleNodeGraph.entityGraphAndDbDataProvider().graph().id()));
    }

    private static void assertIsReset(final AppDatabase appDatabase) {
        assertThat(appDatabase.searchablePreferenceScreenGraphDAO().loadAll(), is(empty()));
    }
}
