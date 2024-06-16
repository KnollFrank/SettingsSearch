package de.KnollFrank.lib.preferencesearch;

import static org.junit.Assert.fail;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.test.core.app.ActivityScenario;

import com.google.common.collect.ImmutableList;

import org.junit.Test;

import java.util.List;

import de.KnollFrank.lib.preferencesearch.common.Preferences;
import de.KnollFrank.preferencesearch.test.TestActivity;

// FK-TODO: zeige zu einer Preference im Suchergebnis auch die PreferenceCategories an, zu der diese Preference gehört. Diese PreferenceCategories sollen nicht anklickbar sein.
public class PreferenceScreensMergerTest {

    @Test
    public void shouldDestructivelyMergeScreens() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(PreferenceScreensMergerTest::destructivelyMergeScreens);
        }
    }

    private static void destructivelyMergeScreens(final FragmentActivity fragmentActivity) {
        // Given
        final PreferenceScreensMerger preferenceScreensMerger = new PreferenceScreensMerger(fragmentActivity);
        final PreferenceFragments preferenceFragments =
                new PreferenceFragments(
                        fragmentActivity,
                        fragmentActivity.getSupportFragmentManager(),
                        TestActivity.FRAGMENT_CONTAINER_VIEW);
        final PreferenceScreen preferenceScreen =
                preferenceFragments
                        .getPreferenceScreenOfFragment(PrefsFragment.class.getName())
                        .get()
                        .preferenceScreen;

        // When
        final PreferenceScreen mergedPreferenceScreen =
                preferenceScreensMerger.destructivelyMergeScreens(
                        ImmutableList.of(preferenceScreen));

        // Then
        final List<Preference> allPreferences = Preferences.getAllChildren(mergedPreferenceScreen);
        fail("Then case not yet implemented");
    }

    public static class PrefsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            final Context context = getPreferenceManager().getContext();
            final PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);
            screen.setTitle("This is PrefsFragment");

            final CheckBoxPreference checkBoxPreference = new CheckBoxPreference(context);
            checkBoxPreference.setKey("fourthfile");
            checkBoxPreference.setSummary("This checkbox is a preference coming from a fourth file");
            checkBoxPreference.setTitle("Checkbox fourth file");

            screen.addPreference(checkBoxPreference);
            setPreferenceScreen(screen);
        }
    }
}