package com.bytehamster.lib.preferencesearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.StringContains.containsString;

import android.os.Looper;

import androidx.preference.Preference;

import com.bytehamster.preferencesearch.test.R;

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
        // Given
        final int preferenceScreen = R.xml.prefs;
        final List<Preference> preferences =
                PreferenceParser
                        .fromContext(TestUtils.getContext())
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
    }
}