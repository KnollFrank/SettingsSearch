package de.KnollFrank.lib.settingssearch.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.jgrapht.Graph;

// FK-TODO: remove?
public class GraphEquality {

    public static <V, E> void assertActualEqualsExpected(final Graph<V, E> actual, final Graph<V, E> expected) {
        assertThat(actual.vertexSet(), is(expected.vertexSet()));
        assertThat(actual.edgeSet(), is(expected.edgeSet()));
    }
}
