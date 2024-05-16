package com.bytehamster.lib.preferencesearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.StringContains.containsString;

import androidx.annotation.XmlRes;

import com.bytehamster.preferencesearch.test.R;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class PreferenceParserTest {

    @Test
    public void shouldSearch() {
        // Given
        final PreferenceParser preferenceParser = getPreferenceParser(R.xml.prefs);
        final String keyword = "Switch";

        // When
        final List<PreferenceItem> preferenceItems = preferenceParser.searchFor(keyword, true);

        // Then
        final List<String> titles =
                preferenceItems
                        .stream()
                        .map(result -> result.title)
                        .collect(Collectors.toList());
        assertThat(titles, hasItem(containsString(keyword)));
    }

    private static PreferenceParser getPreferenceParser(@XmlRes final int preferenceScreen) {
        final PreferenceParser preferenceParser = new PreferenceParser(TestUtils.getContext());
        final SearchConfiguration searchConfiguration = new SearchConfiguration();
        searchConfiguration.index(preferenceScreen);
        searchConfiguration.getFiles().forEach(preferenceParser::addResourceFile);
        return preferenceParser;
    }
}