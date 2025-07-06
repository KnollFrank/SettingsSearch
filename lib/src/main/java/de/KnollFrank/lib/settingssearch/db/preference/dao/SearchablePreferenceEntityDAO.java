package de.KnollFrank.lib.settingssearch.db.preference.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceAndChildren;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceAndChildrens;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceAndPredecessor;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceAndPredecessors;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

// FK-TODO: order methods
@Dao
public abstract class SearchablePreferenceEntityDAO implements SearchablePreferenceEntity.DbDataProvider {

    private final AppDatabase appDatabase;
    // FK-TODO: remove cache?
    private Optional<Map<SearchablePreferenceEntity, Optional<SearchablePreferenceEntity>>> predecessorByPreference = Optional.empty();
    // FK-TODO: remove cache?
    private Optional<Map<SearchablePreferenceEntity, Set<SearchablePreferenceEntity>>> childrenByPreference = Optional.empty();

    public SearchablePreferenceEntityDAO(final AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }

    @Query("SELECT * FROM SearchablePreferenceEntity WHERE id = :id")
    public abstract Optional<SearchablePreferenceEntity> findPreferenceById(final int id);

    public void persist(final SearchablePreferenceEntity... searchablePreferences) {
        _persist(searchablePreferences);
        invalidateCaches();
    }

    public void persist(final Collection<SearchablePreferenceEntity> searchablePreferences) {
        _persist(searchablePreferences);
        invalidateCaches();
    }

    public void remove(final Collection<SearchablePreferenceEntity> preferences) {
        _remove(preferences);
        invalidateCaches();
    }

    public void remove(final SearchablePreferenceEntity... preferences) {
        _remove(preferences);
        invalidateCaches();
    }

    public void update(final SearchablePreferenceEntity... preferences) {
        _update(preferences);
        invalidateCaches();
    }

    @Override
    public Optional<SearchablePreferenceEntity> getPredecessor(final SearchablePreferenceEntity preference) {
        return Maps.get(getPredecessorByPreference(), preference).orElseThrow();
    }

    @Override
    public Set<SearchablePreferenceEntity> getChildren(final SearchablePreferenceEntity preference) {
        return Maps.get(getChildrenByPreference(), preference).orElseThrow();
    }

    @Override
    public SearchablePreferenceScreenEntity getHost(final SearchablePreferenceEntity preference) {
        return appDatabase.searchablePreferenceScreenEntityDAO().getHost(preference);
    }

    public void removeAll() {
        _removeAll();
        invalidateCaches();
    }

    @Query("DELETE FROM SearchablePreferenceEntity")
    protected abstract void _removeAll();

    @Query("SELECT MAX(id) FROM SearchablePreferenceEntity")
    public abstract Optional<Integer> getMaxId();

    @Insert
    protected abstract void _persist(Collection<SearchablePreferenceEntity> searchablePreferences);

    @Insert
    protected abstract void _persist(SearchablePreferenceEntity... searchablePreferences);

    @Transaction
    @Query("SELECT * FROM SearchablePreferenceEntity")
    protected abstract List<PreferenceAndPredecessor> _getPreferencesAndPredecessors();

    @Transaction
    @Query("SELECT * FROM SearchablePreferenceEntity")
    protected abstract List<PreferenceAndChildren> _getPreferencesAndChildren();

    @Delete
    protected abstract void _remove(SearchablePreferenceEntity... preferences);

    @Delete
    public abstract void _remove(final Collection<SearchablePreferenceEntity> preferences);

    @Update
    protected abstract void _update(SearchablePreferenceEntity... preferences);

    private Map<SearchablePreferenceEntity, Set<SearchablePreferenceEntity>> getChildrenByPreference() {
        if (childrenByPreference.isEmpty()) {
            childrenByPreference = Optional.of(computeChildrenByPreference());
        }
        return childrenByPreference.orElseThrow();
    }

    private Map<SearchablePreferenceEntity, Set<SearchablePreferenceEntity>> computeChildrenByPreference() {
        return PreferenceAndChildrens.getChildrenByPreference(
                new HashSet<>(_getPreferencesAndChildren()));
    }

    private Map<SearchablePreferenceEntity, Optional<SearchablePreferenceEntity>> getPredecessorByPreference() {
        if (predecessorByPreference.isEmpty()) {
            predecessorByPreference = Optional.of(computePredecessorByPreference());
        }
        return predecessorByPreference.orElseThrow();
    }

    private Map<SearchablePreferenceEntity, Optional<SearchablePreferenceEntity>> computePredecessorByPreference() {
        return PreferenceAndPredecessors.getPredecessorByPreference(
                new HashSet<>(_getPreferencesAndPredecessors()));
    }

    private void invalidateCaches() {
        predecessorByPreference = Optional.empty();
        childrenByPreference = Optional.empty();
    }
}
