package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.Locale;
import de.KnollFrank.lib.settingssearch.search.LocaleDatabaseTest;

@RunWith(RobolectricTestRunner.class)
public class LocaleDAOTest extends LocaleDatabaseTest {

    @Test
    public void shouldPersistAndLoadLocale() {
        // Given
        final LocaleDAO dao = localeDatabase.localeDAO();
        final Locale locale = new Locale("de");

        // When
        dao.persist(locale);

        // Then
        final Set<Locale> locales = dao.getLocales();
        assertThat(locales, contains(locale));
    }

    @Test
    public void test_persist_ignoreConflicts() {
        // Given
        final LocaleDAO dao = localeDatabase.localeDAO();
        final Locale locale = new Locale("de");

        // When
        dao.persist(locale);

        // And generating a conflict
        dao.persist(locale);

        // Then
        final Set<Locale> locales = dao.getLocales();
        assertThat(locales, contains(locale));
    }
}
