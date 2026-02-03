package de.KnollFrank.lib.settingssearch.fragment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenToSearchablePreferenceScreenConverterTest.initializeFragment;

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
public class PreferenceScreenAdapterTest {

    @Test
    public void test_getPreferenceScreen_searchablePreference() {
        test_getPreferenceScreen(
                PreferenceScreenAdapterTest::addSinglePreferencesScreen,
                true);
    }

    @Test
    public void test_getPreferenceScreen_nonSearchablePreference() {
        test_getPreferenceScreen(
                PreferenceScreenAdapterTest::addSinglePreferencesScreen,
                false);
    }

    @Test
    public void test_getPreferenceScreen_nestedSearchablePreference() {
        test_getPreferenceScreen(
                PreferenceScreenAdapterTest::addNestedPreferenceToScreen,
                true);
    }

    @Test
    public void test_getPreferenceScreen_nestedNonSearchablePreference() {
        test_getPreferenceScreen(
                PreferenceScreenAdapterTest::addNestedPreferenceToScreen,
                false);
    }

    private interface AddPreferencesToScreen {

        void addPreferencesToScreen(final String keyOfSearchableOrNonSearchablePreference,
                                    final PreferenceScreen screen,
                                    final Context context);
    }

    private void test_getPreferenceScreen(final AddPreferencesToScreen addPreferencesToScreen,
                                          final boolean preferenceSearchable) {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final String keyOfSearchableOrNonSearchablePreference = "somePreference";
                final PreferenceFragmentCompat preferenceFragment =
                        (PreferenceFragmentCompat) initializeFragment(
                                new PreferenceFragmentTemplate(
                                        (screen, context) ->
                                                addPreferencesToScreen.addPreferencesToScreen(
                                                        keyOfSearchableOrNonSearchablePreference,
                                                        screen,
                                                        context)),
                                activity);
                final PreferenceSearchablePredicate preferenceSearchablePredicate =
                        new PreferenceSearchablePredicate() {

                            @Override
                            public boolean isPreferenceSearchable(final Preference preference, final PreferenceFragmentCompat hostOfPreference) {
                                return keyOfSearchableOrNonSearchablePreference.equals(preference.getKey()) ?
                                        preferenceSearchable :
                                        true;
                            }
                        };

                // When
                PreferenceScreenAdapter.removeNonSearchablePreferencesFromPreferenceScreenOfPreferenceFragment(preferenceFragment, preferenceSearchablePredicate);

                // Then
                assertThat(preferenceFragment.getPreferenceScreen().findPreference(keyOfSearchableOrNonSearchablePreference) != null, is(preferenceSearchable));
            });
        }
    }

    private static void addSinglePreferencesScreen(final String keyOfSearchableOrNonSearchablePreference, final PreferenceScreen screen, final Context context) {
        final Preference preference = new Preference(context);
        preference.setKey(keyOfSearchableOrNonSearchablePreference);
        screen.addPreference(preference);
    }

    private static void addNestedPreferenceToScreen(final String keyOfSearchableOrNonSearchablePreference,
                                                    final PreferenceScreen screen,
                                                    final Context context) {
        final PreferenceCategory parent = new PreferenceCategory(context);
        screen.addPreference(parent);

        final Preference nestedSearchablePreference = new Preference(context);
        nestedSearchablePreference.setKey(keyOfSearchableOrNonSearchablePreference);
        parent.addPreference(nestedSearchablePreference);
    }
}