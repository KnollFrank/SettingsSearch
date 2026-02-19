package de.KnollFrank.lib.settingssearch.db.preference.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.LanguageCode;
import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesRoomDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviderFactory;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTreeEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.TreeAndDbDataProvider;

@Dao
public abstract class SearchablePreferenceScreenTreeEntityDao implements SearchablePreferenceScreenTreeEntity.DbDataProvider {

    private final SearchablePreferenceScreenEntityDao screenDao;
    private final SearchablePreferenceEntityDao preferenceDao;

    public SearchablePreferenceScreenTreeEntityDao(final PreferencesRoomDatabase preferencesRoomDatabase) {
        this.screenDao = preferencesRoomDatabase.searchablePreferenceScreenEntityDao();
        this.preferenceDao = preferencesRoomDatabase.searchablePreferenceEntityDao();
    }

    @Transaction
    public DatabaseState persistOrReplace(final TreeAndDbDataProvider treeAndDbDataProvider) {
        final DatabaseState removedDatabaseState = removeIfPresent(treeAndDbDataProvider.tree());
        final DatabaseState persistedDatabaseState = persist(treeAndDbDataProvider);
        return removedDatabaseState.combine(persistedDatabaseState);
    }

    public Optional<TreeAndDbDataProvider> findTreeById(final LanguageCode id) {
        return this
                ._findTreeById(id)
                .map(this::createTreeAndDbDataProvider);
    }

    public Set<TreeAndDbDataProvider> loadAll() {
        return this
                ._loadAll()
                .stream()
                .map(this::createTreeAndDbDataProvider)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<SearchablePreferenceScreenEntity> getNodes(final SearchablePreferenceScreenTreeEntity tree) {
        // FK-TODO: add cache?
        return screenDao.findSearchablePreferenceScreensByGraphId(tree.id());
    }

    @Transaction
    public DatabaseState removeAll() {
        final DatabaseState screenDatabaseState = screenDao.removeAll();
        final DatabaseState graphDatabaseState = wrapper.removeAll();
        return screenDatabaseState.combine(graphDatabaseState);
    }

    @Insert
    protected abstract long persistAndReturnInsertedRowId(SearchablePreferenceScreenTreeEntity tree);

    @Delete
    protected abstract int removeAndReturnNumberOfDeletedRows(SearchablePreferenceScreenTreeEntity tree);

    @Query("DELETE FROM SearchablePreferenceScreenTreeEntity")
    protected abstract int removeAllAndReturnNumberOfDeletedRows();

    @Query("SELECT * FROM SearchablePreferenceScreenTreeEntity WHERE id = :id")
    protected abstract Optional<SearchablePreferenceScreenTreeEntity> _findTreeById(LanguageCode id);

    @Query("SELECT * FROM SearchablePreferenceScreenTreeEntity")
    protected abstract List<SearchablePreferenceScreenTreeEntity> _loadAll();

    private TreeAndDbDataProvider createTreeAndDbDataProvider(final SearchablePreferenceScreenTreeEntity tree) {
        return new TreeAndDbDataProvider(
                tree,
                DbDataProviderFactory.createDbDataProvider(this, screenDao, preferenceDao));
    }

    private DatabaseState removeIfPresent(final SearchablePreferenceScreenTreeEntity graph) {
        final var treeFromDB = _findTreeById(graph.id());
        return treeFromDB.isPresent() ?
                remove(treeFromDB.orElseThrow()) :
                DatabaseState.fromDatabaseChanged(false);
    }

    private DatabaseState remove(final SearchablePreferenceScreenTreeEntity tree) {
        final DatabaseState screenDatabaseState = screenDao.remove(screenDao.findSearchablePreferenceScreensByGraphId(tree.id()));
        final DatabaseState treeDatabaseState = wrapper.remove(tree);
        return screenDatabaseState.combine(treeDatabaseState);
    }

    private DatabaseState persist(final TreeAndDbDataProvider treeAndDbDataProvider) {
        final DatabaseState screenDatabaseState =
                screenDao.persist(
                        getScreens(treeAndDbDataProvider),
                        treeAndDbDataProvider.dbDataProvider());
        final DatabaseState graphDatabaseState = wrapper.persist(treeAndDbDataProvider);
        return screenDatabaseState.combine(graphDatabaseState);
    }

    private static Set<SearchablePreferenceScreenEntity> getScreens(final TreeAndDbDataProvider treeAndDbDataProvider) {
        return treeAndDbDataProvider
                .tree()
                .getNodes(treeAndDbDataProvider.dbDataProvider());
    }

    private class Wrapper {

        public DatabaseState persist(final TreeAndDbDataProvider treeAndDbDataProvider) {
            final long insertedRowId = persistAndReturnInsertedRowId(treeAndDbDataProvider.tree());
            return DatabaseStateFactory.fromInsertedRowId(insertedRowId);
        }

        public DatabaseState removeAll() {
            final int numberOfDeletedRows = removeAllAndReturnNumberOfDeletedRows();
            return DatabaseStateFactory.fromNumberOfChangedRows(numberOfDeletedRows);
        }

        public DatabaseState remove(final SearchablePreferenceScreenTreeEntity tree) {
            final int numberOfDeletedRows = removeAndReturnNumberOfDeletedRows(tree);
            return DatabaseStateFactory.fromNumberOfChangedRows(numberOfDeletedRows);
        }
    }

    private final Wrapper wrapper = new Wrapper();
}
