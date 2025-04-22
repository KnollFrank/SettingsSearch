package de.KnollFrank.lib.settingssearch.db.preference.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.db.InMemoryDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.test.SearchablePreferenceEquality;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceDAOTest {

    @Test
    public void shouldPersistAndLoad() {
        // Given
        final SearchablePreferenceDAO searchablePreferenceDAO = new SearchablePreferenceDAO(new InMemoryDatabase());
        final SearchablePreference searchablePreference = createSomeSearchablePreference();

        // When
        searchablePreferenceDAO.persist(Set.of(searchablePreference));
        final SearchablePreference searchablePreferenceActual =
                searchablePreferenceDAO.getPreferenceById(searchablePreference.getId());

        // Then
        SearchablePreferenceEquality.assertActualEqualsExpected(searchablePreferenceActual, searchablePreference);
    }

    private static SearchablePreference createSomeSearchablePreference() {
        return POJOTestFactory.createSearchablePreferencePOJO(
                Optional.of("some title"),
                Optional.of("some summary"),
                Optional.of("some searchable info"),
                Optional.empty());
    }
}
