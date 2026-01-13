package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;
import de.KnollFrank.lib.settingssearch.graph.EntityTreePojoTreeConverter;

public class SearchablePreferenceScreenTreeDAO {

    private final EntityTreePojoTreeConverter entityTreePojoTreeConverter;
    private final SearchablePreferenceScreenTreeEntityDAO delegate;
    private final Map<Locale, Optional<SearchablePreferenceScreenTree>> graphById = new HashMap<>();

    public SearchablePreferenceScreenTreeDAO(final EntityTreePojoTreeConverter entityTreePojoTreeConverter,
                                             final SearchablePreferenceScreenTreeEntityDAO delegate) {
        this.entityTreePojoTreeConverter = entityTreePojoTreeConverter;
        this.delegate = delegate;
    }

    public void persistOrReplace(final SearchablePreferenceScreenTree searchablePreferenceScreenTree) {
        delegate.persistOrReplace(entityTreePojoTreeConverter.convertBackward(searchablePreferenceScreenTree));
        graphById.put(searchablePreferenceScreenTree.locale(), Optional.of(searchablePreferenceScreenTree));
    }

    public Optional<SearchablePreferenceScreenTree> findTreeById(final Locale id) {
        if (!graphById.containsKey(id)) {
            graphById.put(id, _findGraphById(id));
        }
        return graphById.get(id);
    }

    public Set<SearchablePreferenceScreenTree> loadAll() {
        final Set<SearchablePreferenceScreenTree> graphs = _loadAll();
        cache(graphs);
        return graphs;
    }

    public void removeAll() {
        final DatabaseState databaseState = delegate.removeAll();
        if (databaseState.isDatabaseChanged()) {
            invalidateCaches();
        }
    }

    private Optional<SearchablePreferenceScreenTree> _findGraphById(final Locale id) {
        return delegate
                .findGraphById(id)
                .map(entityTreePojoTreeConverter::convertForward);
    }

    private Set<SearchablePreferenceScreenTree> _loadAll() {
        return delegate
                .loadAll()
                .stream()
                .map(entityTreePojoTreeConverter::convertForward)
                .collect(Collectors.toSet());
    }

    private void cache(final Set<SearchablePreferenceScreenTree> graphs) {
        for (final SearchablePreferenceScreenTree graph : graphs) {
            graphById.put(graph.locale(), Optional.of(graph));
        }
    }

    private void invalidateCaches() {
        graphById.clear();
    }
}
