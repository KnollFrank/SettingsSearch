package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.ParameterizedRobolectricTestRunner.Parameter;
import org.robolectric.ParameterizedRobolectricTestRunner.Parameters;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.db.Database;
import de.KnollFrank.lib.settingssearch.db.preference.db.FileDatabaseFactory;
import de.KnollFrank.lib.settingssearch.db.preference.db.InMemoryDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.test.SearchablePreferenceEquality;

@RunWith(ParameterizedRobolectricTestRunner.class)
public class SearchablePreferenceDAOTest {

    enum DatabaseType {
        InMemoryDatabase, FileDatabase
    }

    @Parameter(0)
    public DatabaseType databaseType;

    @Parameters(name = "{0}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(
                new Object[][]{
                        {DatabaseType.InMemoryDatabase},
                        {DatabaseType.FileDatabase}
                });
    }

    @Test
    public void shouldPersistPreference() {
        // Given
        final SearchablePreferenceDAO searchablePreferenceDAO = new SearchablePreferenceDAO(getDatabase(databaseType));
        final SearchablePreference preference = createSomeSearchablePreference();

        // When
        searchablePreferenceDAO.persist(Set.of(preference));

        // Then the preference was persisted at all
        final Optional<SearchablePreference> preferenceFromDb = searchablePreferenceDAO.findPreferenceById(preference.getId());
        assertThat(preferenceFromDb.isPresent(), is(true));

        // And the preference was persisted correctly
        SearchablePreferenceEquality.assertActualEqualsExpected(preferenceFromDb.orElseThrow(), preference);
    }

    @Test
    public void shouldRemovePreference() {
        // Given
        final SearchablePreferenceDAO searchablePreferenceDAO = new SearchablePreferenceDAO(getDatabase(databaseType));
        final SearchablePreference preference = createSomeSearchablePreference();

        // When
        searchablePreferenceDAO.persist(Set.of(preference));
        searchablePreferenceDAO.removePreference(preference.getId());

        // Then
        final boolean removed =
                searchablePreferenceDAO
                        .findPreferenceById(preference.getId())
                        .isEmpty();
        assertThat(removed, is(true));
    }

    @Test
    public void shouldSilentlyRemoveNonExistingPreference() {
        // Given
        final SearchablePreferenceDAO searchablePreferenceDAO = new SearchablePreferenceDAO(getDatabase(databaseType));
        searchablePreferenceDAO.persist(Set.of());
        final int idOfNonExistingPreference = 815;

        // When
        searchablePreferenceDAO.removePreference(idOfNonExistingPreference);

        // Then no exception thrown
    }

    private static SearchablePreference createSomeSearchablePreference() {
        return POJOTestFactory.createSearchablePreferencePOJO(
                Optional.of("some title"),
                Optional.of("some summary"),
                Optional.of("some searchable info"),
                Optional.empty());
    }

    private static Database getDatabase(final DatabaseType databaseType) {
        return switch (databaseType) {
            case InMemoryDatabase -> new InMemoryDatabase();
            case FileDatabase -> createFileDatabase(
                    Locale.GERMAN,
                    InstrumentationRegistry.getInstrumentation().getTargetContext());
        };
    }

    private static Database createFileDatabase(final Locale locale, final Context context) {
        final FileDatabaseFactory fileDatabaseFactory =
                new FileDatabaseFactory(
                        new SearchDatabaseDirectoryIO(context));
        return fileDatabaseFactory.createFileDatabase(locale);
    }
}
