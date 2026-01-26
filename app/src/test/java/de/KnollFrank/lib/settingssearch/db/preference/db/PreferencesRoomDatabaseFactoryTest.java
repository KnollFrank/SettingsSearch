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
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesDatabaseConfig.JournalMode;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.TreeProcessorTestFactory;

@RunWith(RobolectricTestRunner.class)
public class PreferencesRoomDatabaseFactoryTest {

    @Test
    public void shouldGetPreferencesRoomDatabase() {
        doWithFragmentActivity(
                fragmentActivity -> {
                    // Given

                    // When
                    final PreferencesRoomDatabase preferencesRoomDatabase =
                            PreferencesRoomDatabaseFactory.createPreferencesRoomDatabase(
                                    new PreferencesDatabaseConfig<>(
                                            "searchable_preferences.db",
                                            Optional.of(
                                                    new PrepackagedPreferencesDatabase<>(
                                                            new File("database/searchable_preferences_prepackaged.db"),
                                                            TreeProcessorTestFactory.createTreeTransformer())),
                                            JournalMode.AUTOMATIC),
                                    fragmentActivity);

                    // Then
                    assertThat(preferencesRoomDatabase, is(not(nullValue())));
                });
    }
}
