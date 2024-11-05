package de.KnollFrank.lib.settingssearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverterTest.initializeFragment;

import android.content.Context;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceFragmentTemplate;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class SearchablePreferenceScreenProviderTest {

    @Test
    public void test_getPreferenceScreen_searchablePreference() {
        test_getPreferenceScreen(
                SearchablePreferenceScreenProviderTest::addSinglePreferencesScreen,
                true);
    }

    @Test
    public void test_getPreferenceScreen_nonSearchablePreference() {
        test_getPreferenceScreen(
                SearchablePreferenceScreenProviderTest::addSinglePreferencesScreen,
                false);
    }

    @Test
    public void test_getPreferenceScreen_nestedSearchablePreference() {
        test_getPreferenceScreen(
                SearchablePreferenceScreenProviderTest::addNestedPreference2Screen,
                true);
    }

    @Test
    public void test_getPreferenceScreen_nestedNonSearchablePreference() {
        test_getPreferenceScreen(
                SearchablePreferenceScreenProviderTest::addNestedPreference2Screen,
                false);
    }

    private interface AddPreferences2Screen {

        void addPreferences2Screen(final String keyOfSearchableOrNonSearchablePreference,
                                   final PreferenceScreen screen,
                                   final Context context);
    }

    private void test_getPreferenceScreen(final AddPreferences2Screen addPreferences2Screen,
                                          final boolean preferenceSearchable) {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final String keyOfSearchableOrNonSearchablePreference = "somePreference";
                final PreferenceFragmentCompat preferenceFragment =
                        initializeFragment(
                                new PreferenceFragmentTemplate(
                                        (screen, context) ->
                                                addPreferences2Screen.addPreferences2Screen(
                                                        keyOfSearchableOrNonSearchablePreference,
                                                        screen,
                                                        context)),
                                activity);
                final SearchablePreferenceScreenProvider searchablePreferenceScreenProvider =
                        new SearchablePreferenceScreenProvider(
                                new PreferenceSearchablePredicate() {

                                    @Override
                                    public boolean isPreferenceOfHostSearchable(final Preference preference, final PreferenceFragmentCompat host) {
                                        return keyOfSearchableOrNonSearchablePreference.equals(preference.getKey()) ?
                                                preferenceSearchable :
                                                true;
                                    }
                                });

                // When
                final PreferenceScreen preferenceScreen = searchablePreferenceScreenProvider.getPreferenceScreen(preferenceFragment);

                // Then
                assertThat(preferenceScreen.findPreference(keyOfSearchableOrNonSearchablePreference) != null, is(preferenceSearchable));
            });
        }
    }

    private static void addSinglePreferencesScreen(final String keyOfSearchableOrNonSearchablePreference, final PreferenceScreen screen, final Context context) {
        final Preference preference = new Preference(context);
        preference.setKey(keyOfSearchableOrNonSearchablePreference);
        screen.addPreference(preference);
    }

    private static void addNestedPreference2Screen(final String keyOfSearchableOrNonSearchablePreference, final PreferenceScreen screen, final Context context) {
        final PreferenceCategory parent = new PreferenceCategory(context);
        screen.addPreference(parent);

        final Preference nestedSearchablePreference = new Preference(context);
        nestedSearchablePreference.setKey(keyOfSearchableOrNonSearchablePreference);
        parent.addPreference(nestedSearchablePreference);
    }
}