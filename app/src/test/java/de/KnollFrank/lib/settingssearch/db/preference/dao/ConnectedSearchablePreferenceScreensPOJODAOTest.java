package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.ConnectedSearchablePreferenceScreensPOJO;

public class ConnectedSearchablePreferenceScreensPOJODAOTest {

    @Test
    public void shouldPersistAndLoadConnectedSearchablePreferenceScreensPOJO() {
        // Given
        final ConnectedSearchablePreferenceScreensPOJO pojo = POJOTestFactory.createSomeConnectedSearchablePreferenceScreensPOJO();
        final OutputStream outputStream = new ByteArrayOutputStream();

        // When
        ConnectedSearchablePreferenceScreensPOJODAO.persist(pojo, outputStream);
        final ConnectedSearchablePreferenceScreensPOJO pojoActual = ConnectedSearchablePreferenceScreensPOJODAO.load(convert(outputStream));

        // Then
        assertThat(pojoActual, is(pojo));
    }

    private static InputStream convert(final OutputStream outputStream) {
        return new ByteArrayInputStream(outputStream.toString().getBytes(StandardCharsets.UTF_8));
    }
}