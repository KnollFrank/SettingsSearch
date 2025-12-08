package de.KnollFrank.lib.settingssearch.db.preference.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesRoomDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceAndChildren;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceAndChildrens;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceAndPredecessor;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceAndPredecessors;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

@Dao
public abstract class SearchablePreferenceEntityDAO implements SearchablePreferenceEntity.DbDataProvider {

    private final PreferencesRoomDatabase preferencesRoomDatabase;
    // FK-TODO: remove cache?
    private Optional<Map<SearchablePreferenceEntity, Optional<SearchablePreferenceEntity>>> predecessorByPreference = Optional.empty();
    // FK-TODO: remove cache?
    private Optional<Map<SearchablePreferenceEntity, Set<SearchablePreferenceEntity>>> childrenByPreference = Optional.empty();

    public SearchablePreferenceEntityDAO(final PreferencesRoomDatabase preferencesRoomDatabase) {
        this.preferencesRoomDatabase = preferencesRoomDatabase;
    }

    @Transaction
    public DatabaseState persist(final Collection<SearchablePreferenceEntity> searchablePreferences) {
        final DatabaseState databaseState = wrapper.persist(searchablePreferences);
        if (databaseState.isDatabaseChanged()) {
            invalidateCaches();
        }
        return databaseState;
    }

    @Transaction
    public DatabaseState remove(final Collection<SearchablePreferenceEntity> preferences) {
        final DatabaseState databaseState = wrapper.remove(preferences);
        if (databaseState.isDatabaseChanged()) {
            invalidateCaches();
        }
        return databaseState;
    }

    @Override
    public Optional<SearchablePreferenceEntity> getPredecessor(final SearchablePreferenceEntity preference) {
        return Maps
                .get(getPredecessorByPreference(), preference)
                .orElseThrow();
    }

    @Override
    public Set<SearchablePreferenceEntity> getChildren(final SearchablePreferenceEntity preference) {
        return Maps
                .get(getChildrenByPreference(), preference)
                .orElseThrow();
    }

    @Override
    public SearchablePreferenceScreenEntity getHost(final SearchablePreferenceEntity preference) {
        return preferencesRoomDatabase
                .searchablePreferenceScreenEntityDAO()
                .getHost(preference);
    }

    @Transaction
    public DatabaseState removeAll() {
        final DatabaseState databaseState = wrapper.removeAll();
        if (databaseState.isDatabaseChanged()) {
            invalidateCaches();
        }
        return databaseState;
    }

    @Query("SELECT * FROM SearchablePreferenceEntity WHERE id = :id")
    public abstract Optional<SearchablePreferenceEntity> findPreferenceById(final String id);

    @Query("DELETE FROM SearchablePreferenceEntity")
    protected abstract int removeAllAndReturnNumberOfDeletedRows();

    @Insert
    protected abstract List<Long> persistAndReturnInsertedRowIds(Collection<SearchablePreferenceEntity> searchablePreferences);

    @Transaction
    @Query("SELECT * FROM SearchablePreferenceEntity")
    protected abstract List<PreferenceAndPredecessor> getPreferencesAndPredecessors();

    @Transaction
    @Query("SELECT * FROM SearchablePreferenceEntity")
    protected abstract List<PreferenceAndChildren> getPreferencesAndChildren();

    @Query("DELETE FROM SearchablePreferenceEntity WHERE id IN (:ids)")
    protected abstract int removeByIdsInBatchAndReturnNumberOfDeletedRows(Set<String> ids);

    private Map<SearchablePreferenceEntity, Set<SearchablePreferenceEntity>> getChildrenByPreference() {
        if (childrenByPreference.isEmpty()) {
            childrenByPreference = Optional.of(computeChildrenByPreference());
        }
        return childrenByPreference.orElseThrow();
    }

    private Map<SearchablePreferenceEntity, Set<SearchablePreferenceEntity>> computeChildrenByPreference() {
        return PreferenceAndChildrens.getChildrenByPreference(
                new HashSet<>(getPreferencesAndChildren()));
    }

    private Map<SearchablePreferenceEntity, Optional<SearchablePreferenceEntity>> getPredecessorByPreference() {
        if (predecessorByPreference.isEmpty()) {
            predecessorByPreference = Optional.of(computePredecessorByPreference());
        }
        return predecessorByPreference.orElseThrow();
    }

    private Map<SearchablePreferenceEntity, Optional<SearchablePreferenceEntity>> computePredecessorByPreference() {
        return PreferenceAndPredecessors.getPredecessorByPreference(
                new HashSet<>(getPreferencesAndPredecessors()));
    }

    private void invalidateCaches() {
        predecessorByPreference = Optional.empty();
        childrenByPreference = Optional.empty();
    }

    private class Wrapper {

        public DatabaseState persist(final Collection<SearchablePreferenceEntity> searchablePreferences) {
            final List<Long> insertedRowIds = persistAndReturnInsertedRowIds(searchablePreferences);
            return DatabaseStateFactory.fromInsertedRowIds(insertedRowIds);
        }

        public DatabaseState remove(final Collection<SearchablePreferenceEntity> preferences) {
            final int numberOfDeletedRows = removeByIdsInBatchAndReturnNumberOfDeletedRows(getIds(preferences));
            return DatabaseStateFactory.fromNumberOfChangedRows(numberOfDeletedRows);
        }

        public DatabaseState removeAll() {
            final int numberOfDeletedRows = removeAllAndReturnNumberOfDeletedRows();
            return DatabaseStateFactory.fromNumberOfChangedRows(numberOfDeletedRows);
        }

        private static Set<String> getIds(final Collection<SearchablePreferenceEntity> preferences) {
            return preferences
                    .stream()
                    .map(SearchablePreferenceEntity::id)
                    .collect(Collectors.toSet());
        }
    }

    private final Wrapper wrapper = new Wrapper();
}
