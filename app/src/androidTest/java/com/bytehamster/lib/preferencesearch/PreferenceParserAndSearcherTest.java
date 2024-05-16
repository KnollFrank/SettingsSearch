package com.bytehamster.lib.preferencesearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.StringContains.containsString;

import android.os.Looper;

import com.bytehamster.preferencesearch.test.R;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class PreferenceParserAndSearcherTest {

    @Before
    public void setUp() {
        Looper.prepare();
    }

    @Test
    public void shouldSearch() {
        // Given
        final PreferenceParser preferenceParser = new PreferenceParser(TestUtils.getContext());
        preferenceParser.addResourceFile(R.xml.prefs);
        final Searcher searcher = new Searcher(preferenceParser.getPreferenceItems());
        final String keyword = "Switch";

        // When
        final List<PreferenceItem> preferenceItems = searcher.searchFor(keyword, true);

        // Then
        final List<String> titles =
                preferenceItems
                        .stream()
                        .map(result -> result.title)
                        .collect(Collectors.toList());
        assertThat(titles, hasItem(containsString(keyword)));
    }
}