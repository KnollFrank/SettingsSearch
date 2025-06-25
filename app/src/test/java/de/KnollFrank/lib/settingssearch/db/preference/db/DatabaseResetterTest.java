package de.KnollFrank.lib.settingssearch.db.preference.db;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityTestFactory.createSearchablePreference;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;

@RunWith(RobolectricTestRunner.class)
public class DatabaseResetterTest {

    @Test
    public void shouldResetDatabases() {
        // Given
        final AppDatabase germanAppDatabase = getAppDatabase(Locale.GERMAN);
        initialize(germanAppDatabase);

        final AppDatabase chineseAppDatabase = getAppDatabase(Locale.CHINESE);
        initialize(chineseAppDatabase);

        // When
        DatabaseResetter.resetDatabases(Set.of(germanAppDatabase, chineseAppDatabase));

        // Then
        assertIsReset(germanAppDatabase);
        assertIsReset(chineseAppDatabase);
    }

    @Test
    public void shouldResetAllDatabases() {
        // Given
        final AppDatabase germanAppDatabase = getAppDatabase(Locale.GERMAN);
        initialize(germanAppDatabase);

        final AppDatabase chineseAppDatabase = getAppDatabase(Locale.CHINESE);
        initialize(chineseAppDatabase);

        // When
        DatabaseResetter.resetDatabases(ApplicationProvider.<Context>getApplicationContext());

        // Then
        assertIsReset(germanAppDatabase);
        assertIsReset(chineseAppDatabase);
    }

    private static AppDatabase getAppDatabase(final Locale locale) {
        return AppDatabaseFactory.getInstance(locale, ApplicationProvider.getApplicationContext());
    }

    private static void initialize(final AppDatabase appDatabase) {
        final SearchablePreferenceEntity preference =
                createSearchablePreference(
                        "some preference",
                        Optional.empty());
        appDatabase.searchablePreferenceEntityDAO().persist(preference);
        appDatabase.searchDatabaseStateDAO().setSearchDatabaseInitialized(true);
    }

    private static void assertIsReset(final AppDatabase appDatabase) {
        assertThat(appDatabase.searchablePreferenceEntityDAO().loadAll(), is(empty()));
        assertThat(appDatabase.searchDatabaseStateDAO().isSearchDatabaseInitialized(), is(false));
    }
}
