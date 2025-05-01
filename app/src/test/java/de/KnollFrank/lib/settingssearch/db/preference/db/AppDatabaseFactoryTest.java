package de.KnollFrank.lib.settingssearch.db.preference.db;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseFactory.getInstance;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Locale;

@RunWith(RobolectricTestRunner.class)
public class AppDatabaseFactoryTest {

    @Test
    public void shouldGetLocaleSpecificInstanceOfAppDatabase() {
        final AppDatabase germanAppDatabase1 = getInstance(Locale.GERMAN, ApplicationProvider.getApplicationContext());
        assertThat(germanAppDatabase1, is(not(nullValue())));

        final AppDatabase germanAppDatabase2 = getInstance(Locale.GERMAN, ApplicationProvider.getApplicationContext());
        assertThat(germanAppDatabase2, is(germanAppDatabase1));

        final AppDatabase chineseAppDatabase = getInstance(Locale.CHINESE, ApplicationProvider.getApplicationContext());
        assertThat(chineseAppDatabase, is(not(nullValue())));
        assertThat(chineseAppDatabase, is(not(germanAppDatabase1)));
    }
}
