package de.KnollFrank.lib.settingssearch.graph;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.google.common.graph.ImmutableValueGraph;

import java.util.Comparator;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Pair;
import de.KnollFrank.lib.settingssearch.common.graph.Graphs;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTreeEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.TreeAndDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.BundleMatchers;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.LocaleConverter;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
class EntityGraphEquality {

    public static void assertActualEqualsExpected(final TreeAndDbDataProvider actual,
                                                  final TreeAndDbDataProvider expected) {
        assertActualEqualsExpected(actual.tree(), expected.tree());
        final var actualGraph = actual.asGraph();
        final var expectedGraph = expected.asGraph();
        assertActualEqualsExpected(
                Pair.create(
                        actualGraph.graph().nodes(),
                        actual.dbDataProvider()),
                Pair.create(
                        expectedGraph.graph().nodes(),
                        expected.dbDataProvider()));
        assertActualEdgesEqualsExpectedEdges(
                Pair.create(
                        actualGraph,
                        actual.dbDataProvider()),
                Pair.create(
                        expectedGraph,
                        expected.dbDataProvider()));
    }

    private static void assertActualEqualsExpected(final SearchablePreferenceScreenTreeEntity actual, final SearchablePreferenceScreenTreeEntity expected) {
        assertThat(actual.id(), is(expected.id()));
        assertThat(actual.configuration(), BundleMatchers.isEqualTo(expected.configuration()));
    }

    private static void assertActualEqualsExpected(final Pair<Set<SearchablePreferenceScreenEntity>, DbDataProvider> actual,
                                                   final Pair<Set<SearchablePreferenceScreenEntity>, DbDataProvider> expected) {
        assertThat(
                nodes2String(actual.first(), actual.second()),
                is(nodes2String(expected.first(), expected.second())));
    }

    private static void assertActualEdgesEqualsExpectedEdges(final Pair<Tree<SearchablePreferenceScreenEntity, SearchablePreferenceEntity, ImmutableValueGraph<SearchablePreferenceScreenEntity, SearchablePreferenceEntity>>, SearchablePreferenceEntity.DbDataProvider> actual,
                                                             final Pair<Tree<SearchablePreferenceScreenEntity, SearchablePreferenceEntity, ImmutableValueGraph<SearchablePreferenceScreenEntity, SearchablePreferenceEntity>>, SearchablePreferenceEntity.DbDataProvider> expected) {
        assertThat(
                edgesAsStrings(actual.first().graph(), actual.second()),
                is(edgesAsStrings(expected.first().graph(), expected.second())));
    }

    private static String nodes2String(final Set<SearchablePreferenceScreenEntity> nodes,
                                       final DbDataProvider dbDataProvider) {
        return nodes
                .stream()
                .sorted(Comparator.comparing(SearchablePreferenceScreenEntity::id))
                .map(node -> toString(node, dbDataProvider))
                .collect(Collectors.joining(", "));
    }

    private static Set<String> edgesAsStrings(final ImmutableValueGraph<SearchablePreferenceScreenEntity, SearchablePreferenceEntity> graph,
                                              final SearchablePreferenceEntity.DbDataProvider dbDataProvider) {
        return Graphs
                .getEdges(graph)
                .stream()
                .map(edge -> edge.endpointPair().source().id() + "->" + edge.endpointPair().target().id() + ":" + toString(edge.value(), dbDataProvider))
                .collect(Collectors.toSet());
    }

    private static String toString(final SearchablePreferenceScreenEntity entity,
                                   final DbDataProvider dbDataProvider) {
        return new StringJoiner(", ", SearchablePreferenceScreenEntity.class.getSimpleName() + "[", "]")
                .add("id='" + entity.id() + "'")
                .add("host=" + entity.host())
                .add("title=" + entity.title())
                .add("summary=" + entity.summary())
                .add("graphId=" + new LocaleConverter().convertBackward(entity.graphId()))
                .add("allPreferencesOfPreferenceHierarchy=" +
                             toString(
                                     entity.getAllPreferencesOfPreferenceHierarchy(dbDataProvider),
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
