package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.PARENT_KEY;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTestFactory.createSomeSearchablePreferenceScreen;
import static de.KnollFrank.lib.settingssearch.test.SearchablePreferenceScreenEquality.assertActualEqualsExpected;

import androidx.preference.PreferenceFragmentCompat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferences;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceScreenDAOTest extends AppDatabaseTest {

    @Test
    public void shouldPersistAndFindSearchablePreferenceScreenById() {
        // Given
        final SearchablePreferenceScreenDAO dao = appDatabase.searchablePreferenceScreenDAO();
        final SearchablePreferenceScreen screen = createSomeSearchablePreferenceScreen();

        // When
        dao.persist(screen);

        // Then the SearchablePreferenceScreen was persisted at all
        final Optional<SearchablePreferenceScreen> screenFromDb = dao.findSearchablePreferenceScreenById(screen.getId());
        assertThat(screenFromDb.isPresent(), is(true));

        // And the SearchablePreferenceScreen was persisted correctly
        assertActualEqualsExpected(screenFromDb.orElseThrow(), screen);
    }

    @Test
    public void test_findSearchablePreferenceScreenByHost() {
        // Given
        final SearchablePreferenceScreenDAO dao = appDatabase.searchablePreferenceScreenDAO();
        final SearchablePreferenceScreen screen = createSomeSearchablePreferenceScreen();
        dao.persist(screen);

        // When
        final Optional<SearchablePreferenceScreen> screenActual = dao.findSearchablePreferenceScreenByHost(screen.getHost());

        // Then
        assertThat(screenActual, is(Optional.of(screen)));
    }

    @Test
    public void test_findSearchablePreferenceScreenByHost_emptyDatabase() {
        // Given
        final SearchablePreferenceScreenDAO dao = appDatabase.searchablePreferenceScreenDAO();
        final Class<? extends PreferenceFragmentCompat> nonExistingHost = PreferenceFragmentCompat.class;

        // When
        final Optional<SearchablePreferenceScreen> screenActual = dao.findSearchablePreferenceScreenByHost(nonExistingHost);

        // Then
        assertThat(screenActual, is(Optional.empty()));
    }

    @Test
    public void shouldGetHostOfPreferencesOfScreen() {
        // Given
        final SearchablePreferenceScreenDAO dao = appDatabase.searchablePreferenceScreenDAO();
        final SearchablePreferenceScreen screen = createSomeSearchablePreferenceScreen();
        dao.persist(screen);
        final SearchablePreference preference =
                SearchablePreferences
                        .findPreferenceByKey(screen.getAllPreferences(), PARENT_KEY)
                        .orElseThrow();

        // When
        final SearchablePreferenceScreen hostOfPreference = preference.getHost();

        // Then
        assertThat(hostOfPreference, is(screen));
    }
}
