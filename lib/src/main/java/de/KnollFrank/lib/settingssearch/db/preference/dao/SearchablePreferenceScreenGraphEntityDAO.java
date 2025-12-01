package de.KnollFrank.lib.settingssearch.db.preference.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviderFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.GraphAndDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphEntity;

@Dao
public abstract class SearchablePreferenceScreenGraphEntityDAO implements SearchablePreferenceScreenGraphEntity.DbDataProvider {

    private final SearchablePreferenceScreenEntityDAO screenDAO;
    private final SearchablePreferenceEntityDAO preferenceDAO;

    public SearchablePreferenceScreenGraphEntityDAO(final PreferencesDatabase<?> preferencesDatabase) {
        this.screenDAO = preferencesDatabase.searchablePreferenceScreenEntityDAO();
        this.preferenceDAO = preferencesDatabase.searchablePreferenceEntityDAO();
    }

    public void persist(final GraphAndDbDataProvider graphAndDbDataProvider) {
        removeIfPresent(graphAndDbDataProvider.graph());
        _persist(graphAndDbDataProvider);
    }

    public Optional<GraphAndDbDataProvider> findGraphById(final Locale id) {
        return this
                ._findGraphById(id)
                .map(this::createGraphAndDbDataProvider);
    }

    public Set<GraphAndDbDataProvider> loadAll() {
        return this
                ._loadAll()
                .stream()
                .map(this::createGraphAndDbDataProvider)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<SearchablePreferenceScreenEntity> getNodes(final SearchablePreferenceScreenGraphEntity graph) {
        // FK-TODO: add cache?
        return screenDAO.findSearchablePreferenceScreensByGraphId(graph.id());
    }

    public void removeAll() {
        screenDAO.removeAll();
        _removeAll();
    }

    @Insert
    protected abstract void persist(SearchablePreferenceScreenGraphEntity graph);

    @Delete
    protected abstract void _remove(SearchablePreferenceScreenGraphEntity graph);

    @Query("DELETE FROM SearchablePreferenceScreenGraphEntity")
    protected abstract void _removeAll();

    @Query("SELECT * FROM SearchablePreferenceScreenGraphEntity WHERE id = :id")
    protected abstract Optional<SearchablePreferenceScreenGraphEntity> _findGraphById(Locale id);

    @Query("SELECT * FROM SearchablePreferenceScreenGraphEntity")
    protected abstract List<SearchablePreferenceScreenGraphEntity> _loadAll();

    private GraphAndDbDataProvider createGraphAndDbDataProvider(final SearchablePreferenceScreenGraphEntity graph) {
        return new GraphAndDbDataProvider(
                graph,
                DbDataProviderFactory.createDbDataProvider(this, screenDAO, preferenceDAO));
    }

    private void removeIfPresent(final SearchablePreferenceScreenGraphEntity graph) {
        this
                .findGraphById(graph.id())
                .map(GraphAndDbDataProvider::graph)
                .ifPresent(this::remove);
    }

    private void remove(final SearchablePreferenceScreenGraphEntity graph) {
        graph.getNodes(this).forEach(screenDAO::remove);
        _remove(graph);
    }

    private void _persist(final GraphAndDbDataProvider graphAndDbDataProvider) {
        screenDAO.persist(
                getScreens(graphAndDbDataProvider),
                graphAndDbDataProvider.dbDataProvider());
        persist(graphAndDbDataProvider.graph());
    }

    private static Set<SearchablePreferenceScreenEntity> getScreens(final GraphAndDbDataProvider graphAndDbDataProvider) {
        return graphAndDbDataProvider
                .graph()
                .getNodes(graphAndDbDataProvider.dbDataProvider());
    }
}
