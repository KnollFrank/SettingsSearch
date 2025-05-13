package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTestFactory.createSomeSearchablePreferenceScreen;
import static de.KnollFrank.lib.settingssearch.test.SearchablePreferenceScreenEquality.assertActualEqualsExpected;

import androidx.preference.PreferenceFragmentCompat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.HostWithArguments;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

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
    public void test_findSearchablePreferenceScreenByHostWithArguments() {
        // Given
        final SearchablePreferenceScreenDAO dao = appDatabase.searchablePreferenceScreenDAO();
        final SearchablePreferenceScreen screen = createSomeSearchablePreferenceScreen();
        dao.persist(screen);

        // When
        final Optional<SearchablePreferenceScreen> screenActual = dao.findSearchablePreferenceScreenByHostWithArguments(screen.getHostWithArguments());

        // Then
        assertThat(screenActual, is(Optional.of(screen)));
    }

    @Test
    public void test_findSearchablePreferenceScreenByHostWithArguments_emptyDatabase() {
        // Given
        final SearchablePreferenceScreenDAO dao = appDatabase.searchablePreferenceScreenDAO();
        final HostWithArguments nonExistingHostWithArguments = new HostWithArguments(PreferenceFragmentCompat.class, Optional.empty());

        // When
        final Optional<SearchablePreferenceScreen> screenActual = dao.findSearchablePreferenceScreenByHostWithArguments(nonExistingHostWithArguments);

        // Then
        assertThat(screenActual, is(Optional.empty()));
    }
}
