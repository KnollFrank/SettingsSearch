package com.bytehamster.lib.preferencesearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.StringContains.containsString;

import android.os.Looper;

import androidx.preference.Preference;
import androidx.test.core.app.ActivityScenario;

import com.bytehamster.lib.preferencesearch.PreferenceParserTest.PrefsFragment;
import com.bytehamster.preferencesearch.multiplePreferenceScreens.MultiplePreferenceScreensExample;

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
        try (final ActivityScenario<MultiplePreferenceScreensExample> scenario = ActivityScenario.launch(MultiplePreferenceScreensExample.class)) {
            scenario.onActivity(fragmentActivity -> {
                // Given
                final Class<PrefsFragment> preferenceScreen = PrefsFragment.class;
                final List<Preference> preferences =
                        new PreferenceParser(new PreferenceFragmentCompatHelper(fragmentActivity))
                                .parsePreferenceScreen(preferenceScreen);
                final PreferenceSearcher preferenceSearcher =
                        new PreferenceSearcher(PreferenceItems.getPreferenceItems(preferences, preferenceScreen));
                final String keyword = "Switch";

                // When
                final List<PreferenceItem> preferenceItems = preferenceSearcher.searchFor(keyword, true);

                // Then
                final List<String> titles =
                        preferenceItems
                                .stream()
                                .map(result -> result.title)
                                .collect(Collectors.toList());
                assertThat(titles, hasItem(containsString(keyword)));
            });
        }
    }
}