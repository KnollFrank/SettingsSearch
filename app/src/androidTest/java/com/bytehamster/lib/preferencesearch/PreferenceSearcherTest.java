package com.bytehamster.lib.preferencesearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.StringContains.containsString;

import android.os.Looper;

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
        final List<PreferenceItem> parsedPreferenceItems =
                PreferenceParserFactory
                        .fromContext(TestUtils.getContext())
                        .parsePreferenceScreen(R.xml.prefs);
        final PreferenceSearcher preferenceSearcher = new PreferenceSearcher(parsedPreferenceItems);
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