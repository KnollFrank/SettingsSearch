package de.KnollFrank.lib.preferencesearch;

import static de.KnollFrank.lib.preferencesearch.PreferenceScreensMergerTestImplementation.shouldDestructivelyMergeScreens;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.ImmutableList;

import org.junit.Test;

import de.KnollFrank.preferencesearch.R;

public class PreferenceScreensMergerTest {

    @Test
    public void shouldDestructivelyMergeScreens_singleScreen() {
        shouldDestructivelyMergeScreens(
                ImmutableList.of(SingleScreen.Test_preferences.class),
                SingleScreen.Test_preferences_merged.class);
    }

    @Test
    public void shouldDestructivelyMergeScreens_twoScreens() {
        shouldDestructivelyMergeScreens(
                ImmutableList.of(
                        TwoScreens.Test_two_screens_preferences1.class,
                        TwoScreens.Test_two_screens_preferences2.class),
                TwoScreens.Test_two_screens_preferences_merged.class);
    }

    // FK-TODO: zeige zu einer Preference im Suchergebnis auch die PreferenceCategories an, zu der diese Preference gehört. Diese PreferenceCategories sollen nicht anklickbar sein.
    @Test
    public void shouldDestructivelyMergeScreens_retainPreferenceCategories() {
        shouldDestructivelyMergeScreens(
                ImmutableList.of(RetainCategories.Test_retain_categories_preferences.class),
                RetainCategories.Test_retain_categories_merged.class);
    }

    private static class SingleScreen {

        public static class Test_preferences extends PreferenceFragmentCompat {

            @Override
            public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
                addPreferencesFromResource(R.xml.test_preferences);
            }
        }

        public static class Test_preferences_merged extends PreferenceFragmentCompat {

            @Override
            public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
                addPreferencesFromResource(R.xml.test_preferences_merged);
            }
        }
    }

    private static class RetainCategories {

        public static class Test_retain_categories_preferences extends PreferenceFragmentCompat {

            @Override
            public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
                addPreferencesFromResource(R.xml.test_retain_categories_preferences);
            }
        }

        public static class Test_retain_categories_merged extends PreferenceFragmentCompat {

            @Override
            public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
                addPreferencesFromResource(R.xml.test_retain_categories_merged);
            }
        }
    }

    private static class TwoScreens {

        public static class Test_two_screens_preferences1 extends PreferenceFragmentCompat {

            @Override
            public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
                addPreferencesFromResource(R.xml.test_two_screens_preferences1);
            }
        }

        public static class Test_two_screens_preferences2 extends PreferenceFragmentCompat {

            @Override
            public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
                addPreferencesFromResource(R.xml.test_two_screens_preferences2);
            }
        }

        public static class Test_two_screens_preferences_merged extends PreferenceFragmentCompat {

            @Override
            public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
                addPreferencesFromResource(R.xml.test_two_screens_preferences_merged);
            }
        }
    }
}