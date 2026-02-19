package de.KnollFrank.lib.settingssearch.db.preference.dao;

import android.os.PersistableBundle;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.LanguageCode;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTree;
import de.KnollFrank.lib.settingssearch.graph.EntityTreePojoTreeConverter;

public class SearchablePreferenceScreenTreeDao {

    private final EntityTreePojoTreeConverter entityTreePojoTreeConverter;
    private final SearchablePreferenceScreenTreeEntityDao delegate;
    private final Map<LanguageCode, Optional<SearchablePreferenceScreenTree<PersistableBundle>>> treeById = new HashMap<>();

    public SearchablePreferenceScreenTreeDao(final EntityTreePojoTreeConverter entityTreePojoTreeConverter,
                                             final SearchablePreferenceScreenTreeEntityDao delegate) {
        this.entityTreePojoTreeConverter = entityTreePojoTreeConverter;
        this.delegate = delegate;
    }

    public void persistOrReplace(final SearchablePreferenceScreenTree<PersistableBundle> searchablePreferenceScreenTree) {
        delegate.persistOrReplace(entityTreePojoTreeConverter.convertBackward(searchablePreferenceScreenTree));
        treeById.put(searchablePreferenceScreenTree.languageCode(), Optional.of(searchablePreferenceScreenTree));
    }

    public Optional<SearchablePreferenceScreenTree<PersistableBundle>> findTreeById(final LanguageCode id) {
        if (!treeById.containsKey(id)) {
            treeById.put(id, _findTreeById(id));
        }
        return treeById.get(id);
    }

    public Set<SearchablePreferenceScreenTree<PersistableBundle>> loadAll() {
        final Set<SearchablePreferenceScreenTree<PersistableBundle>> graphs = _loadAll();
        cache(graphs);
        return graphs;
    }

    public void removeAll() {
        final DatabaseState databaseState = delegate.removeAll();
        if (databaseState.isDatabaseChanged()) {
            invalidateCaches();
        }
    }

    private Optional<SearchablePreferenceScreenTree<PersistableBundle>> _findTreeById(final LanguageCode id) {
        return delegate
                .findTreeById(id)
                .map(entityTreePojoTreeConverter::convertForward);
    }

    private Set<SearchablePreferenceScreenTree<PersistableBundle>> _loadAll() {
        return delegate
                .loadAll()
                .stream()
                .map(entityTreePojoTreeConverter::convertForward)
                .collect(Collectors.toSet());
    }

    private void cache(final Set<SearchablePreferenceScreenTree<PersistableBundle>> graphs) {
        for (final SearchablePreferenceScreenTree<PersistableBundle> graph : graphs) {
            treeById.put(graph.languageCode(), Optional.of(graph));
        }
    }

    private void invalidateCaches() {
        treeById.clear();
    }
}
