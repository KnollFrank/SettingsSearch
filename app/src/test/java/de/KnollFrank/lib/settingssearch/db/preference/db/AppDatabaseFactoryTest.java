package de.KnollFrank.lib.settingssearch.db.preference.db;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.db.preference.dao.LocaleDAO;

@RunWith(RobolectricTestRunner.class)
public class AppDatabaseFactoryTest {

    @Test
    public void shouldGetLocaleSpecificInstanceOfAppDatabase() {
        final AppDatabase germanAppDatabase1 = AppDatabaseFactory.getInstance(Locale.GERMAN, ApplicationProvider.getApplicationContext());
        assertThat(germanAppDatabase1, is(not(nullValue())));

        final AppDatabase germanAppDatabase2 = AppDatabaseFactory.getInstance(Locale.GERMAN, ApplicationProvider.getApplicationContext());
        assertThat(germanAppDatabase2, is(germanAppDatabase1));

        final AppDatabase chineseAppDatabase = AppDatabaseFactory.getInstance(Locale.CHINESE, ApplicationProvider.getApplicationContext());
        assertThat(chineseAppDatabase, is(not(nullValue())));
        assertThat(chineseAppDatabase, is(not(germanAppDatabase1)));
    }

    @Test
    public void shouldRememberCreationOfLocaleSpecificInstanceOfAppDatabase() {
        shouldRememberCreationOfAppDatabaseForLocale(Locale.GERMAN);
        shouldRememberCreationOfAppDatabaseForLocale(Locale.CHINESE);
    }

    private static void shouldRememberCreationOfAppDatabaseForLocale(final Locale locale) {
        // When creating AppDatabase for languageCode
        AppDatabaseFactory.getInstance(locale, ApplicationProvider.getApplicationContext());

        // Then languageCode is remembered
        final LocaleDAO localeDAO = LocaleDatabase.getInstance(ApplicationProvider.getApplicationContext()).localeDAO();
        assertThat(localeDAO.getLocales(), hasItem(new de.KnollFrank.lib.settingssearch.db.preference.pojo.Locale(locale.getLanguage())));
    }
}
