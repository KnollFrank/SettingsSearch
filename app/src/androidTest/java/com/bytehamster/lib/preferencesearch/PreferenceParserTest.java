package com.bytehamster.lib.preferencesearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.StringContains.containsString;

import com.bytehamster.preferencesearch.test.R;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class PreferenceParserTest {

    @Test
    public void shouldSearch() {
        // Given
        final PreferenceParser preferenceParser = new PreferenceParser(TestUtils.getContext());
        final SearchConfiguration searchConfiguration = new SearchConfiguration();
        searchConfiguration.index(R.xml.prefs);
        for (final SearchConfiguration.SearchIndexItem file : searchConfiguration.getFiles()) {
            preferenceParser.addResourceFile(file);
        }

        // When
        final List<PreferenceItem> preferenceItems = preferenceParser.searchFor("Switch", true);

        // Then
        final List<String> titles =
                preferenceItems
                        .stream()
                        .map(result -> result.title)
                        .collect(Collectors.toList());
        assertThat(titles, hasItem(containsString("Switch")));
    }
}