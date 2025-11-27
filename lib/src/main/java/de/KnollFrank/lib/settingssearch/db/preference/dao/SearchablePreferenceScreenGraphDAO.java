package de.KnollFrank.lib.settingssearch.db.preference.dao;

import org.jgrapht.Graph;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.graph.GraphDifference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;
import de.KnollFrank.lib.settingssearch.graph.EntityGraphPojoGraphConverter;

public class SearchablePreferenceScreenGraphDAO {

    private final EntityGraphPojoGraphConverter entityGraphPojoGraphConverter;
    private final SearchablePreferenceScreenGraphEntityDAO delegate;
    private final Map<Locale, Optional<SearchablePreferenceScreenGraph>> graphById = new HashMap<>();

    public SearchablePreferenceScreenGraphDAO(final EntityGraphPojoGraphConverter entityGraphPojoGraphConverter, final SearchablePreferenceScreenGraphEntityDAO delegate) {
        this.entityGraphPojoGraphConverter = entityGraphPojoGraphConverter;
        this.delegate = delegate;
    }

    public void persist(final SearchablePreferenceScreenGraph searchablePreferenceScreenGraph) {
        delegate.persist(entityGraphPojoGraphConverter.doBackward(searchablePreferenceScreenGraph));
        assertGraphFromDbEqualsGraph(searchablePreferenceScreenGraph.graph(), searchablePreferenceScreenGraph.locale());
        graphById.put(searchablePreferenceScreenGraph.locale(), Optional.of(searchablePreferenceScreenGraph));
    }

    public Optional<SearchablePreferenceScreenGraph> findGraphById(final Locale id) {
        if (!graphById.containsKey(id)) {
            graphById.put(id, _findGraphById(id));
        }
        return graphById.get(id);
    }

    public Set<SearchablePreferenceScreenGraph> loadAll() {
        final Set<SearchablePreferenceScreenGraph> graphs = _loadAll();
        cache(graphs);
        return graphs;
    }

    public void removeAll() {
        delegate.removeAll();
        invalidateCaches();
    }

    private Optional<SearchablePreferenceScreenGraph> _findGraphById(final Locale id) {
        return delegate
                .findGraphById(id)
                .map(entityGraphPojoGraphConverter::doForward);
    }

    private Set<SearchablePreferenceScreenGraph> _loadAll() {
        return delegate
                .loadAll()
                .stream()
                .map(entityGraphPojoGraphConverter::doForward)
                .collect(Collectors.toSet());
    }

    private void cache(final Set<SearchablePreferenceScreenGraph> graphs) {
        for (final SearchablePreferenceScreenGraph graph : graphs) {
            graphById.put(graph.locale(), Optional.of(graph));
        }
    }

    private void invalidateCaches() {
        graphById.clear();
    }

    private void assertGraphFromDbEqualsGraph(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph, final Locale id) {
        final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graphFromDb =
                this
                        ._findGraphById(id)
                        .orElseThrow()
                        .graph();
        final var diff = GraphDifference.between(graph, graphFromDb);
        if (!diff.areEqual()) {
            throw new IllegalStateException("CACHE INCONSISTENCY DETECTED for locale " + id + ":\n" + diff);
        }
    }
}
