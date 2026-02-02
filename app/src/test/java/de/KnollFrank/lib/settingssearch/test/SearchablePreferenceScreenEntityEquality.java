package de.KnollFrank.lib.settingssearch.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

// FK-TODO: remove?
public class SearchablePreferenceScreenEntityEquality {

    private SearchablePreferenceScreenEntityEquality() {
    }

    public static void assertActualEqualsExpected(final SearchablePreferenceScreenEntity actual, final SearchablePreferenceScreenEntity expected) {
        assertThat(actual.id(), is(expected.id()));
        assertThat(actual.host(), is(expected.host()));
        assertThat(actual.title(), is(expected.title()));
        assertThat(actual.summary(), is(expected.summary()));
    }
}
