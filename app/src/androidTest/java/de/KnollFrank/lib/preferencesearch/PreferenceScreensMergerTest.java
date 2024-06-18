package de.KnollFrank.lib.preferencesearch;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.ImmutableList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.List;

import de.KnollFrank.preferencesearch.R;

@RunWith(Parameterized.class)
public class PreferenceScreensMergerTest {

    @Parameters(name = "{0}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(
                new Object[][]{
                        {
                                "SingleScreen",
                                ImmutableList.of(SingleScreen.Test_preferences.class),
                                SingleScreen.Test_preferences_merged.class
                        },
                        {
                                "TwoScreens",
                                ImmutableList.of(
                                        TwoScreens.Test_two_screens_preferences1.class,
                                        TwoScreens.Test_two_screens_preferences2.class),
                                TwoScreens.Test_two_screens_preferences_merged.class
                        },
                        {
                                "RetainCategories",
                                ImmutableList.of(RetainCategories.Test_retain_categories_preferences.class),
                                RetainCategories.Test_retain_categories_merged.class
                        }
                });
    }

    private final List<Class<? extends PreferenceFragmentCompat>> screens2Merge;
    private final Class<? extends PreferenceFragmentCompat> expectedMergedScreen;

    public PreferenceScreensMergerTest(final String testCase,
                                       final List<Class<? extends PreferenceFragmentCompat>> screens2Merge,
                                       final Class<? extends PreferenceFragmentCompat> expectedMergedScreen) {
        this.screens2Merge = screens2Merge;
        this.expectedMergedScreen = expectedMergedScreen;
    }

    @Test
    public void shouldDestructivelyMergeScreens() {
        PreferenceScreensMergerTestImplementation.shouldDestructivelyMergeScreens(screens2Merge, expectedMergedScreen);
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