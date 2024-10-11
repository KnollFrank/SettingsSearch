package de.KnollFrank.lib.settingssearch;

import static org.hamcrest.Matchers.is;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.test.core.app.ActivityScenario;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverterTest.initializeFragment;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.function.BiConsumer;

import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceFragmentTemplate;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceScreenProviderTest {

    @Test
    public void shouldGetPreferenceScreenHavingSearchablePreference() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final String keyOfSearchablePreference = "somePreference";
                final BiConsumer<PreferenceScreen, Context> addPreferences2Screen =
                        new BiConsumer<>() {

                            @Override
                            public void accept(final PreferenceScreen screen, final Context context) {
                                screen.addPreference(createSearchablePreference(context));
                            }

                            private Preference createSearchablePreference(final Context context) {
                                final Preference preference = new Preference(context);
                                preference.setKey(keyOfSearchablePreference);
                                preference.setVisible(true);
                                return preference;
                            }
                        };
                final PreferenceFragmentCompat preferenceFragment =
                        initializeFragment(
                                new PreferenceFragmentTemplate(addPreferences2Screen),
                                activity);
                final SearchablePreferenceScreenProvider searchablePreferenceScreenProvider = new SearchablePreferenceScreenProvider((preference, host) -> true);

                // When
                final PreferenceScreen preferenceScreen = searchablePreferenceScreenProvider.getPreferenceScreen(preferenceFragment);

                // Then
                assertThat(preferenceScreen.findPreference(keyOfSearchablePreference), is(notNullValue()));
            });
        }
    }
}