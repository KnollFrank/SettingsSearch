package de.KnollFrank.lib.settingssearch.db.preference.dao;

import androidx.preference.PreferenceFragmentCompat;
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
import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenAndAllPreferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenAndAllPreferencesHelper;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

// FK-TODO: order methods
@Dao
public abstract class SearchablePreferenceScreenEntityDAO implements SearchablePreferenceScreenEntity.DbDataProvider {

    private final SearchablePreferenceEntityDAO searchablePreferenceDAO;
    // FK-TODO: remove cache?
    private Optional<Map<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>>> allPreferencesBySearchablePreferenceScreen = Optional.empty();
    // FK-TODO: remove cache?
    private Optional<Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity>> hostByPreference = Optional.empty();

    public SearchablePreferenceScreenEntityDAO(final AppDatabase appDatabase) {
        this.searchablePreferenceDAO = appDatabase.searchablePreferenceEntityDAO();
    }

    public void persist(final Collection<SearchablePreferenceScreenEntity> searchablePreferenceScreens) {
        persist(searchablePreferenceScreens, this);
    }

    public void persist(final Collection<SearchablePreferenceScreenEntity> searchablePreferenceScreens,
                        final SearchablePreferenceScreenEntity.DbDataProvider dbDataProvider) {
        searchablePreferenceScreens.forEach(searchablePreferenceScreen -> persist(searchablePreferenceScreen, dbDataProvider));
    }

    public void persist(final SearchablePreferenceScreenEntity searchablePreferenceScreen) {
        persist(searchablePreferenceScreen, this);
    }

    public void remove(final SearchablePreferenceScreenEntity screen) {
        searchablePreferenceDAO.remove(screen.getAllPreferences(this));
        _remove(screen);
    }

    @Delete
    protected abstract void _remove(SearchablePreferenceScreenEntity screen);

    public void persist(final SearchablePreferenceScreenEntity searchablePreferenceScreen,
                        final SearchablePreferenceScreenEntity.DbDataProvider dbDataProvider) {
        searchablePreferenceDAO.persist(searchablePreferenceScreen.getAllPreferences(dbDataProvider));
        _persist(searchablePreferenceScreen);
        invalidateCaches();
    }

    @Query("SELECT * FROM SearchablePreferenceScreenEntity WHERE id = :id")
    public abstract Optional<SearchablePreferenceScreenEntity> findSearchablePreferenceScreenById(final String id);

    public Set<SearchablePreferenceScreenEntity> findSearchablePreferenceScreensByGraphId(final Locale graphId) {
        return new HashSet<>(_findSearchablePreferenceScreensByGraphId(graphId));
    }

    @Query("SELECT * FROM SearchablePreferenceScreenEntity WHERE graphId = :graphId")
    protected abstract List<SearchablePreferenceScreenEntity> _findSearchablePreferenceScreensByGraphId(final Locale graphId);

    // FK-TODO: remove method?
    public Set<SearchablePreferenceScreenEntity> findSearchablePreferenceScreensByHost(final Class<? extends PreferenceFragmentCompat> host) {
        return new HashSet<>(_findSearchablePreferenceScreensByHost(host));
    }

    @Override
    public Set<SearchablePreferenceEntity> getAllPreferences(final SearchablePreferenceScreenEntity screen) {
        return Maps.get(getAllPreferencesBySearchablePreferenceScreen(), screen).orElseThrow();
    }

    @Override
    public SearchablePreferenceScreenEntity getHost(final SearchablePreferenceEntity preference) {
        return Maps.get(getHostByPreference(), preference).orElseThrow();
    }

    public void removeAll() {
        searchablePreferenceDAO.removeAll();
        _removeAll();
        invalidateCaches();
    }

    @Insert
    protected abstract void _persist(SearchablePreferenceScreenEntity searchablePreferenceScreen);

    @Transaction
    @Query("SELECT * FROM SearchablePreferenceScreenEntity")
    protected abstract List<SearchablePreferenceScreenAndAllPreferences> _getSearchablePreferenceScreenAndAllPreferences();

    // FK-TODO: remove method?
    @Query("SELECT * FROM SearchablePreferenceScreenEntity WHERE host = :host")
    protected abstract List<SearchablePreferenceScreenEntity> _findSearchablePreferenceScreensByHost(final Class<? extends PreferenceFragmentCompat> host);

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
