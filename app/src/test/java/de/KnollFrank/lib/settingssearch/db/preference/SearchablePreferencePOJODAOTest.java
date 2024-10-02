package de.KnollFrank.lib.settingssearch.db.preference;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SearchablePreferencePOJODAOTest {

    @Test
    public void shouldPersistAndLoadSearchablePreferencePOJO() {
        // Given
        final SearchablePreferencePOJO someSearchablePreferencePOJO = createSomeSearchablePreferencePOJO();
        final OutputStream outputStream = new ByteArrayOutputStream();

        // When
        SearchablePreferencePOJODAO.persist(someSearchablePreferencePOJO, outputStream);
        final SearchablePreferencePOJO searchablePreferenceActual = SearchablePreferencePOJODAO.load(convert(outputStream));

        // Then
        assertThat(searchablePreferenceActual, is(someSearchablePreferencePOJO));
    }

    private static SearchablePreferencePOJO createSomeSearchablePreferencePOJO() {
        return new SearchablePreferencePOJO(
                "some key",
                androidx.preference.R.drawable.ic_arrow_down_24dp,
                androidx.preference.R.layout.preference,
                "some summary",
                "some title",
                4713,
                "some fragment",
                true,
                "some searchableInfo",
                List.of(
                        new SearchablePreferencePOJO(
                                "some key 2",
                                androidx.preference.R.drawable.ic_arrow_down_24dp,
                                androidx.preference.R.layout.preference,
                                "some summary 2",
                                "some title 2",
                                4714,
                                "some fragment 2",
                                true,
                                "some searchableInfo 2",
                                List.of())));
    }

    private static InputStream convert(final OutputStream outputStream) {
        return new ByteArrayInputStream(outputStream.toString().getBytes(StandardCharsets.UTF_8));
    }
}