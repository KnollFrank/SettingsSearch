package de.KnollFrank.lib.settingssearch.db.preference.converter;

import static org.hamcrest.MatcherAssert.assertThat;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.Matchers.is;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreference2POJOConverterTest {

    @Test
    public void shouldConvert2POJO() {
        // Given
        final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final SearchablePreference searchablePreference = new SearchablePreference(context, Optional.of("some searchable info"));

        // When
        final SearchablePreferencePOJO searchablePreferencePOJO = SearchablePreference2POJOConverter.convert2POJO(searchablePreference);

        // Then
        assertThat(searchablePreferencePOJO.uniqueId(), is(0));
    }
}
