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

import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesRoomDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviderFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.GraphAndDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphEntity;

@Dao
public abstract class SearchablePreferenceScreenGraphEntityDAO implements SearchablePreferenceScreenGraphEntity.DbDataProvider {

    private final SearchablePreferenceScreenEntityDAO screenDAO;
    private final SearchablePreferenceEntityDAO preferenceDAO;

    public SearchablePreferenceScreenGraphEntityDAO(final PreferencesRoomDatabase preferencesRoomDatabase) {
        this.screenDAO = preferencesRoomDatabase.searchablePreferenceScreenEntityDAO();
        this.preferenceDAO = preferencesRoomDatabase.searchablePreferenceEntityDAO();
    }

    public DatabaseState persistOrReplace(final GraphAndDbDataProvider graphAndDbDataProvider) {
        final DatabaseState removedDatabaseState = removeIfPresent(graphAndDbDataProvider.graph());
        final DatabaseState persistedDatabaseState = persist(graphAndDbDataProvider);
        return removedDatabaseState.combine(persistedDatabaseState);
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

    public DatabaseState removeAll() {
        final DatabaseState screenDatabaseState = screenDAO.removeAll();
        final DatabaseState graphDatabaseState = wrapper.removeAll();
        return screenDatabaseState.combine(graphDatabaseState);
    }

    @Insert
    protected abstract long persistAndReturnInsertedRowId(SearchablePreferenceScreenGraphEntity graph);

    @Delete
    protected abstract int removeAndReturnNumberOfDeletedRows(SearchablePreferenceScreenGraphEntity graph);

    @Query("DELETE FROM SearchablePreferenceScreenGraphEntity")
    protected abstract int removeAllAndReturnNumberOfDeletedRows();

    @Query("SELECT * FROM SearchablePreferenceScreenGraphEntity WHERE id = :id")
    protected abstract Optional<SearchablePreferenceScreenGraphEntity> _findGraphById(Locale id);

    @Query("SELECT * FROM SearchablePreferenceScreenGraphEntity")
    protected abstract List<SearchablePreferenceScreenGraphEntity> _loadAll();

    private GraphAndDbDataProvider createGraphAndDbDataProvider(final SearchablePreferenceScreenGraphEntity graph) {
        return new GraphAndDbDataProvider(
                graph,
                DbDataProviderFactory.createDbDataProvider(this, screenDAO, preferenceDAO));
    }

    private DatabaseState removeIfPresent(final SearchablePreferenceScreenGraphEntity graph) {
        final var graphFromDB = _findGraphById(graph.id());
        return graphFromDB.isPresent() ?
                remove(graphFromDB.orElseThrow()) :
                DatabaseState.fromDatabaseChanged(false);
    }

    private DatabaseState remove(final SearchablePreferenceScreenGraphEntity graph) {
        final DatabaseState screenDatabaseState = screenDAO.remove(screenDAO.findSearchablePreferenceScreensByGraphId(graph.id()));
        final DatabaseState graphDatabaseState = wrapper.remove(graph);
        return screenDatabaseState.combine(graphDatabaseState);
    }

    private DatabaseState persist(final GraphAndDbDataProvider graphAndDbDataProvider) {
        final DatabaseState screenDatabaseState =
                screenDAO.persist(
                        getScreens(graphAndDbDataProvider),
                        graphAndDbDataProvider.dbDataProvider());
        final DatabaseState graphDatabaseState = wrapper.persist(graphAndDbDataProvider);
        return screenDatabaseState.combine(graphDatabaseState);
    }

    private static Set<SearchablePreferenceScreenEntity> getScreens(final GraphAndDbDataProvider graphAndDbDataProvider) {
        return graphAndDbDataProvider
                .graph()
                .getNodes(graphAndDbDataProvider.dbDataProvider());
    }

    private class Wrapper {

        public DatabaseState persist(final GraphAndDbDataProvider graphAndDbDataProvider) {
            final long insertedRowId = persistAndReturnInsertedRowId(graphAndDbDataProvider.graph());
            return DatabaseStateFactory.fromInsertedRowId(insertedRowId);
        }

        public DatabaseState removeAll() {
            final int numberOfDeletedRows = removeAllAndReturnNumberOfDeletedRows();
            return DatabaseStateFactory.fromNumberOfChangedRows(numberOfDeletedRows);
        }

        public DatabaseState remove(final SearchablePreferenceScreenGraphEntity graph) {
            final int numberOfDeletedRows = removeAndReturnNumberOfDeletedRows(graph);
            return DatabaseStateFactory.fromNumberOfChangedRows(numberOfDeletedRows);
        }
    }

    private final Wrapper wrapper = new Wrapper();
}
