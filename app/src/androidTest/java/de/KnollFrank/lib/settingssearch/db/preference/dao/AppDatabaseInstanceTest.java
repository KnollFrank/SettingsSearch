package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabase;

@RunWith(AndroidJUnit4.class)
public class AppDatabaseInstanceTest {

    @Test
    public void shouldGetLocaleSpecificInstanceOfAppDatabase() {
        final AppDatabase germanAppDatabase1 = AppDatabase.getInstance(ApplicationProvider.getApplicationContext(), Locale.GERMAN);
        assertThat(germanAppDatabase1, is(not(nullValue())));

        final AppDatabase germanAppDatabase2 = AppDatabase.getInstance(ApplicationProvider.getApplicationContext(), Locale.GERMAN);
        assertThat(germanAppDatabase2, is(germanAppDatabase1));

        final AppDatabase chineseAppDatabase = AppDatabase.getInstance(ApplicationProvider.getApplicationContext(), Locale.CHINESE);
        assertThat(chineseAppDatabase, is(not(nullValue())));
        assertThat(chineseAppDatabase, is(not(germanAppDatabase1)));
    }
}
