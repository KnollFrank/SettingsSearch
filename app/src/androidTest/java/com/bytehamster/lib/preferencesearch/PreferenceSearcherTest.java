package com.bytehamster.lib.preferencesearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.StringContains.containsString;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;

import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.test.core.app.ActivityScenario;

import com.bytehamster.preferencesearch.test.TestActivity;

import org.hamcrest.Matcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class PreferenceSearcherTest {

    @BeforeClass
    public static void beforeClass() {
        Looper.prepare();
    }

    @AfterClass
    public static void afterClass() {
        Looper.getMainLooper().quitSafely();
    }

    @Test
    public void shouldSearch() {
        final String keyword = "fourth";
        testSearch(keyword, hasItem(containsString(keyword)));
    }

    @Test
    public void shouldNotFind() {
        final String keyword = "third";
        testSearch(keyword, not(hasItem(containsString(keyword))));
    }

    private static void testSearch(final String keyword, final Matcher<Iterable<? super String>> titlesMatcher) {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(fragmentActivity -> {
                // Given
                final Class<PrefsFragment> preferenceScreen = PrefsFragment.class;
                final List<Preference> preferences =
                        new PreferenceParser(new PreferenceFragments(fragmentActivity, TestActivity.FRAGMENT_CONTAINER_VIEW))
                                .parsePreferenceScreen(preferenceScreen);
                final PreferenceSearcher preferenceSearcher =
                        new PreferenceSearcher(PreferenceItems.getPreferenceItems(preferences, preferenceScreen));

                // When
                final List<PreferenceItem> preferenceItems = preferenceSearcher.searchFor(keyword, false);

                // Then
                final List<String> titles =
                        preferenceItems
                                .stream()
                                .map(result -> result.title)
                                .collect(Collectors.toList());
                assertThat(titles, titlesMatcher);
            });
        }
    }

    public static class PrefsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            final Context context = getPreferenceManager().getContext();
            final PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);

            final CheckBoxPreference checkBoxPreference = new CheckBoxPreference(context);
            checkBoxPreference.setKey("fourthfile");
            checkBoxPreference.setSummary("This checkbox is a preference coming from a fourth file");
            checkBoxPreference.setTitle("Checkbox fourth file");

            screen.addPreference(checkBoxPreference);
            setPreferenceScreen(screen);
        }
    }
}