package de.KnollFrank.lib.settingssearch.db.preference.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesRoomDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenAndAllPreferencesHelper;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenAndAllPreferencesOfPreferenceHierarchy;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

@Dao
public abstract class SearchablePreferenceScreenEntityDAO implements SearchablePreferenceScreenEntity.DbDataProvider {

    private final SearchablePreferenceEntityDAO searchablePreferenceDAO;
    // FK-TODO: remove cache?
    private Optional<Map<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>>> allPreferencesBySearchablePreferenceScreen = Optional.empty();
    // FK-TODO: remove cache?
    private Optional<Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity>> hostByPreference = Optional.empty();

    public SearchablePreferenceScreenEntityDAO(final PreferencesRoomDatabase preferencesRoomDatabase) {
        this.searchablePreferenceDAO = preferencesRoomDatabase.searchablePreferenceEntityDAO();
    }

    public void persist(final Collection<SearchablePreferenceScreenEntity> searchablePreferenceScreens,
                        final SearchablePreferenceScreenEntity.DbDataProvider dbDataProvider) {
        for (final var searchablePreferenceScreen : searchablePreferenceScreens) {
            persist(searchablePreferenceScreen, dbDataProvider);
        }
        invalidateCaches();
    }

    public void persist(final SearchablePreferenceScreenEntity searchablePreferenceScreen,
                        final SearchablePreferenceScreenEntity.DbDataProvider dbDataProvider) {
        searchablePreferenceDAO.persist(searchablePreferenceScreen.getAllPreferencesOfPreferenceHierarchy(dbDataProvider));
        _persist(searchablePreferenceScreen);
        invalidateCaches();
    }

    public void remove(final SearchablePreferenceScreenEntity screen) {
        searchablePreferenceDAO.remove(screen.getAllPreferencesOfPreferenceHierarchy(this));
        _remove(screen);
        invalidateCaches();
    }

    public Set<SearchablePreferenceScreenEntity> findSearchablePreferenceScreensByGraphId(final Locale graphId) {
        // FK-TODO: add cache?
        return new HashSet<>(_findSearchablePreferenceScreensByGraphId(graphId));
    }

    @Override
    public Set<SearchablePreferenceEntity> getAllPreferencesOfPreferenceHierarchy(final SearchablePreferenceScreenEntity screen) {
        return Maps
                .get(getAllPreferencesBySearchablePreferenceScreen(), screen)
                .orElseThrow();
    }

    @Override
    public SearchablePreferenceScreenEntity getHost(final SearchablePreferenceEntity preference) {
        return Maps
                .get(getHostByPreference(), preference)
                .orElseThrow();
    }

    public void removeAll() {
        searchablePreferenceDAO.removeAll();
        _removeAll();
        invalidateCaches();
    }

    @Delete
    protected abstract void _remove(SearchablePreferenceScreenEntity screen);

    @Query("SELECT * FROM SearchablePreferenceScreenEntity WHERE graphId = :graphId")
    protected abstract List<SearchablePreferenceScreenEntity> _findSearchablePreferenceScreensByGraphId(final Locale graphId);

    @Insert
    protected abstract void _persist(SearchablePreferenceScreenEntity searchablePreferenceScreen);

    @Transaction
    @Query("SELECT * FROM SearchablePreferenceScreenEntity")
    protected abstract List<SearchablePreferenceScreenAndAllPreferencesOfPreferenceHierarchy> _getSearchablePreferenceScreenAndAllPreferences();

    @Query("DELETE FROM SearchablePreferenceScreenEntity")
    protected abstract void _removeAll();

    private Map<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>> getAllPreferencesBySearchablePreferenceScreen() {
        if (allPreferencesBySearchablePreferenceScreen.isEmpty()) {
            allPreferencesBySearchablePreferenceScreen = Optional.of(computeAllPreferencesBySearchablePreferenceScreen());
        }
        return allPreferencesBySearchablePreferenceScreen.orElseThrow();
    }

    private Map<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>> computeAllPreferencesBySearchablePreferenceScreen() {
        return SearchablePreferenceScreenAndAllPreferencesHelper.getAllPreferencesBySearchablePreferenceScreen(
                new HashSet<>(_getSearchablePreferenceScreenAndAllPreferences()));
    }

    private Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity> getHostByPreference() {
        if (hostByPreference.isEmpty()) {
            hostByPreference = Optional.of(computeHostByPreference());
        }
        return hostByPreference.orElseThrow();
    }

    private Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity> computeHostByPreference() {
        return SearchablePreferenceScreenAndAllPreferencesHelper.getHostByPreference(
                new HashSet<>(_getSearchablePreferenceScreenAndAllPreferences()));
    }

    private void invalidateCaches() {
        allPreferencesBySearchablePreferenceScreen = Optional.empty();
        hostByPreference = Optional.empty();
    }
}
