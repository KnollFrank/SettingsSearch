package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphTestFactory.PARENT_KEY;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTestFactory.createSomeSearchablePreferenceScreen;
import static de.KnollFrank.lib.settingssearch.test.SearchablePreferenceScreenEntityEquality.assertActualEqualsExpected;

import androidx.preference.PreferenceFragmentCompat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.Pair;
import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseTest;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntities;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceScreenDAOTest extends AppDatabaseTest {

    @Test
    public void shouldPersistAndFindSearchablePreferenceScreenById() {
        // Given
        final SearchablePreferenceScreenEntityDAO dao = appDatabase.searchablePreferenceScreenEntityDAO();
        final Pair<SearchablePreferenceScreenEntity, DbDataProvider> screen = createSomeSearchablePreferenceScreen();

        // When
        dao.persist(screen.first(), screen.second());

        // Then the SearchablePreferenceScreen was persisted at all
        final Optional<SearchablePreferenceScreenEntity> screenFromDb = dao.findSearchablePreferenceScreenById(screen.first().id());
        assertThat(screenFromDb.isPresent(), is(true));

        // And the SearchablePreferenceScreen was persisted correctly
        assertActualEqualsExpected(screenFromDb.orElseThrow(), screen.first());
    }

    @Test
    public void test_findSearchablePreferenceScreensByHost() {
        // Given
        final SearchablePreferenceScreenEntityDAO dao = appDatabase.searchablePreferenceScreenEntityDAO();
        final Pair<SearchablePreferenceScreenEntity, DbDataProvider> screen = createSomeSearchablePreferenceScreen();
        dao.persist(screen.first(), screen.second());

        // When
        final Set<SearchablePreferenceScreenEntity> screenActual = dao.findSearchablePreferenceScreensByHost(screen.first().host());

        // Then
        assertThat(screenActual, contains(screen.first()));
    }

    @Test
    public void test_findSearchablePreferenceScreensByHost_emptyDatabase() {
        // Given
        final SearchablePreferenceScreenEntityDAO dao = appDatabase.searchablePreferenceScreenEntityDAO();
        final Class<? extends PreferenceFragmentCompat> nonExistingHost = PreferenceFragmentCompat.class;

        // When
        final Set<SearchablePreferenceScreenEntity> screenActual = dao.findSearchablePreferenceScreensByHost(nonExistingHost);

        // Then
        assertThat(screenActual, is(empty()));
    }

    @Test
    public void shouldGetHostOfPreferencesOfScreen() {
        // Given
        final SearchablePreferenceScreenEntityDAO dao = appDatabase.searchablePreferenceScreenEntityDAO();
        final Pair<SearchablePreferenceScreenEntity, DbDataProvider> screen = createSomeSearchablePreferenceScreen();
        dao.persist(screen.first(), screen.second());
        final SearchablePreferenceEntity preference =
                SearchablePreferenceEntities
                        .findPreferenceByKey(screen.first().getAllPreferences(dao), PARENT_KEY)
                        .orElseThrow();

        // When
        final SearchablePreferenceScreenEntity hostOfPreference = preference.getHost(appDatabase.searchablePreferenceEntityDAO());

        // Then
        assertThat(hostOfPreference, is(screen.first()));
    }
}
