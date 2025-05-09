package de.KnollFrank.lib.settingssearch.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class SearchablePreferenceScreenEquality {

    public static void assertActualEqualsExpected(final SearchablePreferenceScreen actual, final SearchablePreferenceScreen expected) {
        assertThat(actual.getId(), is(expected.getId()));
        assertThat(actual.getParentId(), is(expected.getParentId()));
        assertThat(actual.getTitle(), is(expected.getTitle()));
        assertThat(actual.getSummary(), is(expected.getSummary()));
    }
}
