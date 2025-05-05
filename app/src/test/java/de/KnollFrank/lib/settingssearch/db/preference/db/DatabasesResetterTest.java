package de.KnollFrank.lib.settingssearch.db.preference.db;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.POJOTestFactory.createSearchablePreferencePOJO;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Locale;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.dao.TestPreferenceFragment;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

@RunWith(RobolectricTestRunner.class)
public class DatabasesResetterTest {

    @Test
    public void shouldResetDatabase() {
        // Given
        final AppDatabase appDatabase = AppDatabaseFactory.getInstance(Locale.GERMAN, ApplicationProvider.getApplicationContext());
        final SearchablePreference preference =
                createSearchablePreferencePOJO(
                        "some preference",
                        TestPreferenceFragment.class,
                        Optional.empty());
        appDatabase.searchablePreferenceDAO().persist(preference);
        appDatabase.searchDatabaseStateDAO().setSearchDatabaseInitialized(true);

        // When
        DatabasesResetter.resetDatabase(appDatabase);

        // Then
        assertThat(appDatabase.searchablePreferenceDAO().loadAll(), is(empty()));
        assertThat(appDatabase.searchDatabaseStateDAO().isSearchDatabaseInitialized(), is(false));
    }
}
