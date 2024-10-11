package de.KnollFrank.lib.settingssearch;

import static org.hamcrest.Matchers.is;

import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.test.core.app.ActivityScenario;

import static org.hamcrest.MatcherAssert.assertThat;

import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverterTest.initializeFragment;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceFragmentTemplate;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceScreenProviderTest {

    @Test
    public void test_getPreferenceScreen_searchablePreference() {
        test_getPreferenceScreen(true);
    }

    @Test
    public void test_getPreferenceScreen_nonSearchablePreference() {
        test_getPreferenceScreen(false);
    }

    private static void test_getPreferenceScreen(final boolean preferenceSearchable) {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final String keyOfPreference = "somePreference";
                final Function<Context, Preference> preferenceFactory =
                        context -> {
                            final Preference preference = new Preference(context);
                            preference.setKey(keyOfPreference);
                            return preference;
                        };
                final IsPreferenceSearchable isPreferenceSearchable = (preference, host) -> keyOfPreference.equals(preference.getKey()) && preferenceSearchable;
                final PreferenceFragmentCompat preferenceFragment =
                        getPreferenceFragmentCompat(preferenceFactory, activity);
                final SearchablePreferenceScreenProvider searchablePreferenceScreenProvider =
                        new SearchablePreferenceScreenProvider(isPreferenceSearchable);

                // When
                final PreferenceScreen preferenceScreen = searchablePreferenceScreenProvider.getPreferenceScreen(preferenceFragment);

                // Then
                assertThat(preferenceScreen.findPreference(keyOfPreference) != null, is(preferenceSearchable));
            });
        }
    }

    private static PreferenceFragmentCompat getPreferenceFragmentCompat(final Function<Context, Preference> preferenceFactory,
                                                                        final FragmentActivity activity) {
        return initializeFragment(
                new PreferenceFragmentTemplate(
                        (screen, context) ->
                                screen.addPreference(preferenceFactory.apply(context))),
                activity);
    }
}