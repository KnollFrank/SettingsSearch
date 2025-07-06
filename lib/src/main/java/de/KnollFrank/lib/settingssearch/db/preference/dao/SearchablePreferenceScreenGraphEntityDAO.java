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

// FK-TODO: order methods
@Dao
public abstract class SearchablePreferenceScreenGraphEntityDAO implements SearchablePreferenceScreenGraphEntity.DbDataProvider {

    private final SearchablePreferenceScreenEntityDAO screenDAO;
    private final SearchablePreferenceEntityDAO preferenceDAO;

    public SearchablePreferenceScreenGraphEntityDAO(final AppDatabase appDatabase) {
        this.screenDAO = appDatabase.searchablePreferenceScreenEntityDAO();
        this.preferenceDAO = appDatabase.searchablePreferenceEntityDAO();
    }

    public void persist(final GraphAndDbDataProvider graphAndDbDataProvider) {
        this
                .findGraphById(graphAndDbDataProvider.graph().id())
                .map(GraphAndDbDataProvider::graph)
                .ifPresent(this::remove);
        _persist(graphAndDbDataProvider);
    }

    public void remove(final SearchablePreferenceScreenGraphEntity graph) {
        graph.getNodes(this).forEach(screenDAO::remove);
        _remove(graph);
    }

    public Optional<GraphAndDbDataProvider> findGraphById(final Locale id) {
        return this
                ._findGraphById(id)
                .map(graph ->
                             new GraphAndDbDataProvider(
                                     graph,
                                     DbDataProviderFactory.createDbDataProvider(this, screenDAO, preferenceDAO)));
    }

    @Override
    public Set<SearchablePreferenceScreenEntity> getNodes(final SearchablePreferenceScreenGraphEntity graph) {
        return screenDAO.findSearchablePreferenceScreensByGraphId(graph.id());
    }

    @Delete
    protected abstract void _remove(SearchablePreferenceScreenGraphEntity graph);

    public void removeAll() {
        screenDAO.removeAll();
        _removeAll();
    }

    @Insert
    protected abstract void persist(SearchablePreferenceScreenGraphEntity graph);

    @Query("SELECT * FROM SearchablePreferenceScreenGraphEntity WHERE id = :id")
    protected abstract Optional<SearchablePreferenceScreenGraphEntity> _findGraphById(final Locale id);

    @Query("DELETE FROM SearchablePreferenceScreenGraphEntity")
    protected abstract void _removeAll();

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
