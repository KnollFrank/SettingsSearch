package de.KnollFrank.lib.settingssearch.db.preference.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Preferences;
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
                assertThat(searchablePreference.getKey(), is(pojo.key()));
                // FK-TODO: handle correctly: assertThat(actual.getIcon(), is(expected.iconResId()));
                assertThat(searchablePreference.getLayoutResource(), is(pojo.layoutResId()));
                assertThat(searchablePreference.getSummary(), is(pojo.summary()));
                assertThat(searchablePreference.getTitle(), is(pojo.title()));
                assertThat(searchablePreference.getWidgetLayoutResource(), is(pojo.widgetLayoutResId()));
                assertThat(searchablePreference.getFragment(), is(pojo.fragment()));
                assertThat(searchablePreference.isVisible(), is(pojo.visible()));
                assertThat(searchablePreference.getSearchableInfo(), is(Optional.ofNullable(pojo.searchableInfo())));
                assertThat(searchablePreference.getExtras().get(key), is(value));
                final List<Preference> allChildren = Preferences.getAllChildren(searchablePreference);
            });
        }
    }

    private static Bundle createBundle(final String key, final String value) {
        final Bundle bundle = new Bundle();
        bundle.putString(key, value);
        return bundle;
    }
}