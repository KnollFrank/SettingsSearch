package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferencePOJODAOTest.convert;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferencePOJODAOTest.createSomeSearchablePreferencePOJO;
import static de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenPOJODAOTest.createSomeSearchablePreferenceScreenPOJO;

import androidx.preference.PreferenceFragmentCompat;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.ConnectedSearchablePreferenceScreensPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferencePathPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;

public class ConnectedSearchablePreferenceScreensPOJODAOTest {

    @Test
    public void shouldPersistAndLoadConnectedSearchablePreferenceScreensPOJO() {
        // Given
        final ConnectedSearchablePreferenceScreensPOJO pojo = createSomeConnectedSearchablePreferenceScreensPOJO();
        final OutputStream outputStream = new ByteArrayOutputStream();

        // When
        ConnectedSearchablePreferenceScreensPOJODAO.persist(pojo, outputStream);
        final ConnectedSearchablePreferenceScreensPOJO pojoActual = ConnectedSearchablePreferenceScreensPOJODAO.load(convert(outputStream));

        // Then
        assertThat(pojoActual, is(pojo));
    }

    private static ConnectedSearchablePreferenceScreensPOJO createSomeConnectedSearchablePreferenceScreensPOJO() {
        return new ConnectedSearchablePreferenceScreensPOJO(
                Set.of(createSomePreferenceScreenWithHostClassPOJO()),
                Map.of(createSomeSearchablePreferencePOJO(), createSomePreferencePathPOJO()));
    }

    private static PreferenceScreenWithHostClassPOJO createSomePreferenceScreenWithHostClassPOJO() {
        return new PreferenceScreenWithHostClassPOJO(
                createSomeSearchablePreferenceScreenPOJO(),
                PreferenceFragmentCompat.class);
    }

    private static PreferencePathPOJO createSomePreferencePathPOJO() {
        return new PreferencePathPOJO(List.of(createSomeSearchablePreferencePOJO()));
    }
}