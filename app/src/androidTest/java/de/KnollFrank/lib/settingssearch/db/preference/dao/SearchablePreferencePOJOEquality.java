package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class SearchablePreferencePOJOEquality {

    private final SearchablePreferencePOJODAO dao;

    public SearchablePreferencePOJOEquality(final SearchablePreferencePOJODAO dao) {
        this.dao = dao;
    }

    public void assertActualEqualsExpected(final SearchablePreferencePOJO actual, final SearchablePreferencePOJO expected) {
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
        assertThat(actual.getHost(), is(expected.getHost()));
        // assertThat(equalBundles(actual.getExtras(), expected.getExtras()), is(true));
        assertActualListEqualsExpectedList(actual.getChildren(dao), expected.getChildren(dao));
        assertThat(actual.getPredecessor(dao), is(expected.getPredecessor(dao)));
    }

    public void assertActualListEqualsExpectedList(final List<SearchablePreferencePOJO> actuals, final List<SearchablePreferencePOJO> expecteds) {
        assertThat(actuals.size(), is(expecteds.size()));
        for (int i = 0; i < actuals.size(); i++) {
            assertActualEqualsExpected(actuals.get(i), expecteds.get(i));
        }
    }
}
