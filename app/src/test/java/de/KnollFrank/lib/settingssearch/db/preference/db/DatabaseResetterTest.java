package de.KnollFrank.lib.settingssearch.db.preference.db;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static de.KnollFrank.lib.settingssearch.test.TestHelper.doWithFragmentActivity;

import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.File;
import java.util.Locale;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesDatabaseConfig.JournalMode;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.PersistableBundleTestFactory;
import de.KnollFrank.settingssearch.Configuration;
import de.KnollFrank.settingssearch.ConfigurationBundleConverter;

@RunWith(RobolectricTestRunner.class)
public class DatabaseResetterTest {

    @Test
    public void shouldResetDatabase() {
        doWithFragmentActivity(
                fragmentActivity -> {
                    // Given
                    final Locale locale = Locale.GERMAN;
                    final PreferencesRoomDatabase<Configuration> preferencesRoomDatabase = getPreferencesDatabase(fragmentActivity, locale);
                    initialize(preferencesRoomDatabase, locale);

                    // When
                    DatabaseResetter.resetDatabase(preferencesRoomDatabase);

                    // Then
                    assertIsReset(preferencesRoomDatabase, fragmentActivity);
                });
    }

    private static PreferencesRoomDatabase<Configuration> getPreferencesDatabase(final FragmentActivity activity, final Locale locale) {
        return PreferencesRoomDatabaseFactory.createPreferencesRoomDatabase(
                new PreferencesDatabaseConfig<>(
                        "searchable_preferences.db",
                        Optional.of(
                                new PrepackagedPreferencesDatabase<>(
                                        new File("database/searchable_preferences_prepackaged.db"),
                                        (graph, actualConfiguration, activityContext) -> graph)),
                        JournalMode.AUTOMATIC),
                PersistableBundleTestFactory.createSomeConfiguration(),
                locale,
                new ConfigurationBundleConverter(),
                activity);
    }

    private static void initialize(final PreferencesRoomDatabase<Configuration> preferencesRoomDatabase, final Locale locale) {
        final var singleNodeGraph =
                SearchablePreferenceScreenGraphTestFactory.createSingleNodeGraph(
                        PreferenceFragmentCompat.class,
                        locale,
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
        preferencesRoomDatabase
                .searchablePreferenceScreenGraphRepository()
                .persist(
                        new SearchablePreferenceScreenGraph(
                                singleNodeGraph.pojoGraph(),
                                singleNodeGraph.entityGraphAndDbDataProvider().graph().id(),
                                singleNodeGraph.entityGraphAndDbDataProvider().graph().configuration()));
    }

    private static void assertIsReset(final PreferencesRoomDatabase<Configuration> preferencesRoomDatabase,
                                      final FragmentActivity activityContext) {
        assertThat(preferencesRoomDatabase.searchablePreferenceScreenGraphRepository().loadAll(null, activityContext), is(empty()));
    }
}
