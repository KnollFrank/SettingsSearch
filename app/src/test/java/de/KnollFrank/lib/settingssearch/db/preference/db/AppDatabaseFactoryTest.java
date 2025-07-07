package de.KnollFrank.lib.settingssearch.db.preference.db;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class AppDatabaseFactoryTest {

    @Test
    public void shouldGetAppDatabase() {
        // Given

        // When
        final AppDatabase appDatabase = AppDatabaseFactory.getInstance(ApplicationProvider.getApplicationContext());

        // Then
        assertThat(appDatabase, is(not(nullValue())));
    }
}
