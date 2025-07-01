package de.KnollFrank.lib.settingssearch.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;

public class SearchablePreferenceEquality {

    public static void assertActualEqualsExpected(final SearchablePreferenceEntity actual, final SearchablePreferenceEntity expected) {
        assertThat(actual.id(), is(expected.id()));
        assertThat(actual.key(), is(expected.key()));
        assertThat(actual.iconResourceIdOrIconPixelData(), is(expected.iconResourceIdOrIconPixelData()));
        assertThat(actual.layoutResId(), is(expected.layoutResId()));
        assertThat(actual.summary(), is(expected.summary()));
        assertThat(actual.title(), is(expected.title()));
        assertThat(actual.widgetLayoutResId(), is(expected.widgetLayoutResId()));
        assertThat(actual.fragment(), is(expected.fragment()));
        assertThat(actual.visible(), is(expected.visible()));
        assertThat(actual.searchableInfo(), is(expected.searchableInfo()));
        assertThat(actual.searchablePreferenceScreenId(), equalTo(expected.searchablePreferenceScreenId()));
        assertThat(actual.parentId(), is(expected.parentId()));
        assertThat(actual.predecessorId(), is(expected.predecessorId()));
    }
}
