package de.KnollFrank.lib.settingssearch.db.preference.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.os.Bundle;

import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.dao.POJOTestFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceFromPOJOConverterTest {

    @Test
    public void shouldConvertSearchablePreferencePOJO2SearchablePreference() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final String key = "someKey";
                final String value = "someValue";
                final SearchablePreferencePOJO pojo =
                        POJOTestFactory.createSomeSearchablePreferencePOJO(
                                createBundle(key, value));

                // When
                final SearchablePreference searchablePreference = SearchablePreferenceFromPOJOConverter.convertFromPOJO(pojo, activity);

                // Then
                assertEquals(searchablePreference, pojo);
            });
        }
    }

    private static void assertEquals(final SearchablePreference actual, final SearchablePreferencePOJO expected) {
        assertThat(actual.getKey(), is(expected.key()));
        // FK-TODO: handle correctly: assertThat(actual.getIcon(), is(expected.iconResId()));
        assertThat(actual.getLayoutResource(), is(expected.layoutResId()));
        assertThat(actual.getSummary(), is(expected.summary()));
        assertThat(actual.getTitle(), is(expected.title()));
        assertThat(actual.getWidgetLayoutResource(), is(expected.widgetLayoutResId()));
        assertThat(actual.getFragment(), is(expected.fragment()));
        assertThat(actual.isVisible(), is(expected.visible()));
        assertThat(actual.getSearchableInfo(), is(Optional.ofNullable(expected.searchableInfo())));
        assertThat(equalBundles(actual.getExtras(), expected.extras()), is(true));
    }

    // adapted from https://stackoverflow.com/a/13238729
    private static boolean equalBundles(final Bundle one, final Bundle two) {
        if (one.size() != two.size())
            return false;

        Set<String> setOne = new HashSet<>(one.keySet());
        setOne.addAll(two.keySet());
        Object valueOne;
        Object valueTwo;

        for (String key : setOne) {
            if (!one.containsKey(key) || !two.containsKey(key))
                return false;

            valueOne = one.get(key);
            valueTwo = two.get(key);
            if (valueOne instanceof Bundle && valueTwo instanceof Bundle &&
                    !equalBundles((Bundle) valueOne, (Bundle) valueTwo)) {
                return false;
            } else if (valueOne == null) {
                if (valueTwo != null)
                    return false;
            } else if (!valueOne.equals(valueTwo))
                return false;
        }

        return true;
    }

    private static Bundle createBundle(final String key, final String value) {
        final Bundle bundle = new Bundle();
        bundle.putString(key, value);
        return bundle;
    }
}