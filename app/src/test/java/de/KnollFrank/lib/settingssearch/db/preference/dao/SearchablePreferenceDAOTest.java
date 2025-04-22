package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.db.InMemoryDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.test.SearchablePreferenceEquality;

@RunWith(RobolectricTestRunner.class)
// FK-TODO: alle Tests mit InMemoryDatabase UND FileDatabase durchführen
public class SearchablePreferenceDAOTest {

    @Test
    public void shouldPersistPreference() {
        // Given
        final SearchablePreferenceDAO searchablePreferenceDAO = new SearchablePreferenceDAO(new InMemoryDatabase());
        final SearchablePreference preference = createSomeSearchablePreference();

        // When
        searchablePreferenceDAO.persist(Set.of(preference));

        // Then preference was persisted at all
        final Optional<SearchablePreference> preferenceFromDb = searchablePreferenceDAO.findPreferenceById(preference.getId());
        assertThat(preferenceFromDb.isPresent(), is(true));

        // And preference was persisted correctly
        SearchablePreferenceEquality.assertActualEqualsExpected(preferenceFromDb.orElseThrow(), preference);
    }

    @Test
    public void shouldRemovePreference() {
        // Given
        final SearchablePreferenceDAO searchablePreferenceDAO = new SearchablePreferenceDAO(new InMemoryDatabase());
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
        final SearchablePreferenceDAO searchablePreferenceDAO = new SearchablePreferenceDAO(new InMemoryDatabase());
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
}
