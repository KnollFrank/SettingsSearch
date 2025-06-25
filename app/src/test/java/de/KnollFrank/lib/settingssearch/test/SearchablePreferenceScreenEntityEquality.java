package de.KnollFrank.lib.settingssearch.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import androidx.preference.PreferenceFragmentCompat;

import org.hamcrest.Matchers;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class SearchablePreferenceScreenEntityEquality {

    public static void assertActualEqualsExpected(final SearchablePreferenceScreenEntity actual, final SearchablePreferenceScreenEntity expected) {
        assertThat(actual.getId(), is(expected.getId()));
        assertThat(actual.getHost(), Matchers.<Class<? extends PreferenceFragmentCompat>>is(expected.getHost()));
        assertThat(actual.getTitle(), is(expected.getTitle()));
        assertThat(actual.getSummary(), is(expected.getSummary()));
    }
}
