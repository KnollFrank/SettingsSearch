package de.KnollFrank.lib.settingssearch.db.preference.db;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static de.KnollFrank.lib.settingssearch.test.TestHelper.doWithFragmentActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.File;
import java.util.Locale;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesDatabaseConfig.JournalMode;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.PersistableBundleTestFactory;
import de.KnollFrank.settingssearch.Configuration;
import de.KnollFrank.settingssearch.ConfigurationBundleConverter;

@RunWith(RobolectricTestRunner.class)
public class PreferencesRoomDatabaseFactoryTest {

    @Test
    public void shouldGetPreferencesDatabase() {
        doWithFragmentActivity(
                fragmentActivity -> {
                    // Given

                    // When
                    final PreferencesRoomDatabase<Configuration> preferencesRoomDatabase =
                            PreferencesRoomDatabaseFactory.createPreferencesRoomDatabase(
                                    new PreferencesDatabaseConfig<>(
                                            "searchable_preferences.db",
                                            Optional.of(
                                                    new PrepackagedPreferencesDatabase<>(
                                                            new File("database/searchable_preferences_prepackaged.db"),
                                                            (graph, actualConfiguration, activityContext) -> graph)),
                                            JournalMode.AUTOMATIC),
                                    fragmentActivity,
                                    Locale.GERMAN,
                                    PersistableBundleTestFactory.createSomeConfiguration(),
                                    new ConfigurationBundleConverter());

                    // Then
                    assertThat(preferencesRoomDatabase, is(not(nullValue())));
                });
    }
}
