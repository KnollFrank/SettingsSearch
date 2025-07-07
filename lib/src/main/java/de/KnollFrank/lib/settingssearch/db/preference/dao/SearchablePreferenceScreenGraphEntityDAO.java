package de.KnollFrank.lib.settingssearch.db.preference.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviderFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.GraphAndDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphEntity;

@Dao
public abstract class SearchablePreferenceScreenGraphEntityDAO implements SearchablePreferenceScreenGraphEntity.DbDataProvider {

    private final SearchablePreferenceScreenEntityDAO screenDAO;
    private final SearchablePreferenceEntityDAO preferenceDAO;

    public SearchablePreferenceScreenGraphEntityDAO(final AppDatabase appDatabase) {
        this.screenDAO = appDatabase.searchablePreferenceScreenEntityDAO();
        this.preferenceDAO = appDatabase.searchablePreferenceEntityDAO();
    }

    public void persist(final GraphAndDbDataProvider graphAndDbDataProvider) {
        removeIfPresent(graphAndDbDataProvider.graph());
        _persist(graphAndDbDataProvider);
    }

    public Optional<GraphAndDbDataProvider> findGraphById(final Locale id) {
        return _findGraphById(id).map(this::createGraphAndDbDataProvider);
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
    protected abstract Optional<SearchablePreferenceScreenGraphEntity> _findGraphById(final Locale id);

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
