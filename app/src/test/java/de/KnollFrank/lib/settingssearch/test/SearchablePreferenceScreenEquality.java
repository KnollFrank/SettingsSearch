package de.KnollFrank.lib.settingssearch.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import androidx.preference.PreferenceFragmentCompat;

import org.hamcrest.Matchers;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class SearchablePreferenceScreenEquality {

    public static void assertActualEqualsExpected(final SearchablePreferenceScreen actual, final SearchablePreferenceScreen expected) {
        assertThat(actual.id(), is(expected.id()));
        assertThat(actual.host(), Matchers.<Class<? extends PreferenceFragmentCompat>>is(expected.host()));
        assertThat(actual.title(), is(expected.title()));
        assertThat(actual.summary(), is(expected.summary()));
        SearchablePreferenceEquality.assertActualEqualsExpected(actual.allPreferencesOfPreferenceHierarchy(), expected.allPreferencesOfPreferenceHierarchy());
    }
}
