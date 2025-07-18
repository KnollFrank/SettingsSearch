package de.KnollFrank.lib.settingssearch.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import androidx.preference.PreferenceFragmentCompat;

import org.hamcrest.Matchers;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class SearchablePreferenceScreenEntityEquality {

    public static void assertActualEqualsExpected(final SearchablePreferenceScreenEntity actual, final SearchablePreferenceScreenEntity expected) {
        assertThat(actual.id(), is(expected.id()));
        assertThat(actual.host(), Matchers.<Class<? extends PreferenceFragmentCompat>>is(expected.host()));
        assertThat(actual.title(), is(expected.title()));
        assertThat(actual.summary(), is(expected.summary()));
    }
}
