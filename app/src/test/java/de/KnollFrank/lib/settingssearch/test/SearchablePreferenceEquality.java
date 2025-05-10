package de.KnollFrank.lib.settingssearch.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.pojo.BundleEquality.equalBundles;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public class SearchablePreferenceEquality {

    public static void assertActualEqualsExpected(final SearchablePreference actual, final SearchablePreference expected) {
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
        assertThat(actual.getHost(), equalTo(expected.getHost()));
        assertThat(equalBundles(actual.getExtras(), expected.getExtras()), is(true));
        assertThat(actual.getParentId(), is(expected.getParentId()));
        assertThat(actual.getPredecessorId(), is(expected.getPredecessorId()));
    }
}
