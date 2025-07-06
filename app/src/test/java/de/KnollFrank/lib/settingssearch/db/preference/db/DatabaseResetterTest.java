package de.KnollFrank.lib.settingssearch.db.preference.db;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.content.Context;

import androidx.preference.PreferenceFragmentCompat;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory;
import de.KnollFrank.lib.settingssearch.graph.GraphForLocale;

@RunWith(RobolectricTestRunner.class)
public class DatabaseResetterTest {

    @Test
    public void shouldResetDatabases() {
        // Given
        final Locale german = Locale.GERMAN;
        final AppDatabase germanAppDatabase = getAppDatabase(german);
        initialize(germanAppDatabase);

        final Locale chinese = Locale.CHINESE;
        final AppDatabase chineseAppDatabase = getAppDatabase(chinese);
        initialize(chineseAppDatabase);

        // When
        DatabaseResetter.resetDatabases(Set.of(germanAppDatabase, chineseAppDatabase));

        // Then
        assertIsReset(germanAppDatabase, german);
        assertIsReset(chineseAppDatabase, chinese);
    }

    @Test
    public void shouldResetAllDatabases() {
        // Given
        final Locale german = Locale.GERMAN;
        final AppDatabase germanAppDatabase = getAppDatabase(german);
        initialize(germanAppDatabase);

        final Locale chinese = Locale.CHINESE;
        final AppDatabase chineseAppDatabase = getAppDatabase(chinese);
        initialize(chineseAppDatabase);

        // When
        DatabaseResetter.resetDatabases(ApplicationProvider.<Context>getApplicationContext());

        // Then
        assertIsReset(germanAppDatabase, german);
        assertIsReset(chineseAppDatabase, chinese);
    }

    private static AppDatabase getAppDatabase(final Locale locale) {
        return AppDatabaseFactory.getInstance(locale, ApplicationProvider.getApplicationContext());
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

    private static void assertIsReset(final AppDatabase appDatabase, final Locale locale) {
        assertThat(appDatabase.searchablePreferenceScreenGraphDAO().findGraphById(locale), is(Optional.empty()));
    }
}
