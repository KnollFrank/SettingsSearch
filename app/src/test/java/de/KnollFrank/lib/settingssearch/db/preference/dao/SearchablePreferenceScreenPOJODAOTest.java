package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferencePOJODAOTest.convert;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferencePOJODAOTest.createSomeSearchablePreferencePOJO;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenPOJO;

public class SearchablePreferenceScreenPOJODAOTest {

    @Test
    public void shouldPersistAndLoadSearchablePreferenceScreenPOJO() {
        // Given
        final SearchablePreferenceScreenPOJO pojo = createSomeSearchablePreferenceScreenPOJO();
        final OutputStream outputStream = new ByteArrayOutputStream();

        // When
        SearchablePreferenceScreenPOJODAO.persist(pojo, outputStream);
        final SearchablePreferenceScreenPOJO pojoActual = SearchablePreferenceScreenPOJODAO.load(convert(outputStream));

        // Then
        assertThat(pojoActual, is(pojo));
    }

    public static SearchablePreferenceScreenPOJO createSomeSearchablePreferenceScreenPOJO() {
        return new SearchablePreferenceScreenPOJO(List.of(createSomeSearchablePreferencePOJO()));
    }
}