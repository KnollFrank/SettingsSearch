package de.KnollFrank.lib.settingssearch.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class SearchablePreferenceScreenGraphEquality {

    public static void assertActualEqualsExpected(final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> actual,
                                                  final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> expected) {
        assertThat(actual.vertexSet(), is(expected.vertexSet()));
        assertThat(actual.edgeSet(), is(expected.edgeSet()));
    }
}
