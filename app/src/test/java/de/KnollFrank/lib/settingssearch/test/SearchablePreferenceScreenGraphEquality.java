package de.KnollFrank.lib.settingssearch.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class SearchablePreferenceScreenGraphEquality {

    public static void assertActualEqualsExpected(final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEdge> actual,
                                                  final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEdge> expected) {
        assertThat(actual.vertexSet(), is(expected.vertexSet()));
        assertThat(actual.edgeSet(), is(expected.edgeSet()));
    }
}
