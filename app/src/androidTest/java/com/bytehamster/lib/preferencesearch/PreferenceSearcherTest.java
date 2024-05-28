package com.bytehamster.lib.preferencesearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.StringContains.containsString;

import android.os.Looper;

import androidx.preference.Preference;
import androidx.test.core.app.ActivityScenario;

import com.bytehamster.lib.preferencesearch.PreferenceParserTest.PrefsFragment;
import com.bytehamster.preferencesearch.multiplePreferenceScreens.TestActivity;

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
                // FK-TODO: do not use "foreign" class PrefsFragment
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
}