package de.KnollFrank.lib.settingssearch.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public class SearchablePreferenceEquality {

    public static void assertActualEqualsExpected(final SearchablePreference actual,
                                                  final SearchablePreference expected) {
        assertThat(actual.getId(), is(expected.getId()));
        assertThat(actual.getKey(), is(expected.getKey()));
        assertThat(actual.getIconResourceIdOrIconPixelData(), is(expected.getIconResourceIdOrIconPixelData()));
        assertThat(actual.getLayoutResId(), is(expected.getLayoutResId()));
        assertThat(actual.getSummary(), is(expected.getSummary()));
        assertThat(actual.getTitle(), is(expected.getTitle()));
        assertThat(actual.getWidgetLayoutResId(), is(expected.getWidgetLayoutResId()));
        assertThat(actual.getFragment(), is(expected.getFragment()));
        assertThat(actual.isVisible(), is(expected.isVisible()));
        assertThat(actual.getSearchableInfo(), is(expected.getSearchableInfo()));
        assertActualEqualsExpected(actual.getChildren(), expected.getChildren());
    }

    public static void assertActualEqualsExpected(final Set<SearchablePreference> actual,
                                                  final Set<SearchablePreference> expected) {
        Lists
                .zip(sortById(actual), sortById(expected))
                .forEach(actual_expected -> assertActualEqualsExpected(actual_expected.first, actual_expected.second));
    }

    private static List<SearchablePreference> sortById(final Set<SearchablePreference> searchablePreferences) {
        return searchablePreferences
                .stream()
                .sorted(Comparator.comparing(SearchablePreference::getId))
                .collect(Collectors.toList());
    }
}
