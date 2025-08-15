package de.KnollFrank.lib.settingssearch.db.preference.db;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.File;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseConfig.JournalMode;

@RunWith(RobolectricTestRunner.class)
public class AppDatabaseFactoryTest {

    @Test
    public void shouldGetAppDatabase() {
        // Given

        // When
        final AppDatabase appDatabase =
                AppDatabaseFactory.createAppDatabase(
                        new AppDatabaseConfig(
                                "searchable_preferences.db",
                                Optional.of(new File("database/searchable_preferences_prepackaged.db")),
                                JournalMode.AUTOMATIC),
                        ApplicationProvider.getApplicationContext());

        // Then
        assertThat(appDatabase, is(not(nullValue())));
    }
}
