package de.KnollFrank.lib.settingssearch.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.jgrapht.Graph;

import java.util.Comparator;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Pair;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

class EntityGraphEquality {

    public static void assertActualEqualsExpected(final EntityGraphAndDbDataProvider actual,
                                                  final EntityGraphAndDbDataProvider expected) {
        assertActualEqualsExpected(
                Pair.create(
                        actual.entityGraph().vertexSet(),
                        actual.dbDataProvider()),
                Pair.create(
                        expected.entityGraph().vertexSet(),
                        expected.dbDataProvider()));
        assertActualEdgesEqualsExpectedEdges(
                Pair.create(
                        actual.entityGraph(),
                        actual.dbDataProvider()),
                Pair.create(
                        expected.entityGraph(),
                        expected.dbDataProvider()));
    }

    private static void assertActualEqualsExpected(final Pair<Set<SearchablePreferenceScreenEntity>, DbDataProvider> nodesAndDbDataProvidersActual,
                                                   final Pair<Set<SearchablePreferenceScreenEntity>, DbDataProvider> nodesAndDbDataProvidersExpected) {
        assertThat(
                nodes2String(nodesAndDbDataProvidersActual.first(), nodesAndDbDataProvidersActual.second()),
                is(equalTo(nodes2String(nodesAndDbDataProvidersExpected.first(), nodesAndDbDataProvidersExpected.second()))));
    }

    private static void assertActualEdgesEqualsExpectedEdges(final Pair<Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge>, SearchablePreferenceEntity.DbDataProvider> actual,
                                                             final Pair<Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge>, SearchablePreferenceEntity.DbDataProvider> expected) {
        final Set<String> expectedEdgesRepr = edgesAsStrings(expected.first(), expected.second());
        final Set<String> actualEdgesRepr = edgesAsStrings(actual.first(), actual.second());
        assertThat(
                actualEdgesRepr,
                is(equalTo(expectedEdgesRepr)));
    }

    private static String nodes2String(final Set<SearchablePreferenceScreenEntity> nodes,
                                       final DbDataProvider dbDataProvider) {
        return nodes
                .stream()
                .sorted(Comparator.comparing(SearchablePreferenceScreenEntity::getId))
                .map(node -> toString(node, dbDataProvider))
                .collect(Collectors.joining(", "));
    }

    private static Set<String> edgesAsStrings(final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> graph,
                                              final SearchablePreferenceEntity.DbDataProvider dbDataProvider) {
        return graph
                .edgeSet()
                .stream()
                .map(edge -> graph.getEdgeSource(edge).getId() + "->" + graph.getEdgeTarget(edge).getId() + ":" + toString(edge.preference, dbDataProvider))
                .collect(Collectors.toSet());
    }

    private static String toString(final SearchablePreferenceScreenEntity entity,
                                   final DbDataProvider dbDataProvider) {
        return new StringJoiner(", ", SearchablePreferenceScreenEntity.class.getSimpleName() + "[", "]")
                .add("id='" + entity.getId() + "'")
                .add("host=" + entity.getHost())
                .add("title=" + entity.getTitle())
                .add("summary=" + entity.getSummary())
                .add("allPreferences=" +
                        toString(
                                entity.getAllPreferences(dbDataProvider),
                                dbDataProvider))
                .toString();
    }

    private static String toString(final Set<SearchablePreferenceEntity> entities,
                                   final SearchablePreferenceEntity.DbDataProvider dbDataProvider) {
        return entities
                .stream()
                .sorted(Comparator.comparing(SearchablePreferenceEntity::id))
                .map(entity -> toString(entity, dbDataProvider))
                .collect(Collectors.joining(", "));
    }

    private static String toString(final SearchablePreferenceEntity entity,
                                   final SearchablePreferenceEntity.DbDataProvider dbDataProvider) {
        return new StringJoiner(", ", SearchablePreferenceEntity.class.getSimpleName() + "[", "]")
                .add("id=" + entity.id())
                .add("key='" + entity.key() + "'")
                .add("title=" + entity.title())
                .add("summary=" + entity.summary())
                .add("searchableInfo=" + entity.searchableInfo())
                .add("fragment=" + entity.fragment())
                .add("visible=" + entity.visible())
                .add("parentId=" + entity.parentId())
                .add("predecessorId=" + entity.predecessorId())
                .add("searchablePreferenceScreenId='" + entity.searchablePreferenceScreenId() + "'")
                .add("children=" + toString(entity.getChildren(dbDataProvider), dbDataProvider))
                .toString();
    }
}
