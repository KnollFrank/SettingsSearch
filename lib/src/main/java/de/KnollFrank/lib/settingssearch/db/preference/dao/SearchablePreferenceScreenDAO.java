package de.KnollFrank.lib.settingssearch.db.preference.dao;

import androidx.preference.PreferenceFragmentCompat;
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

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenAndAllPreferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenAndAllPreferencesHelper;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

@Dao
public abstract class SearchablePreferenceScreenDAO implements SearchablePreferenceScreenEntity.DbDataProvider {

    private final SearchablePreferenceDAO searchablePreferenceDAO;
    private final SearchablePreferenceScreenDAOSetter daoSetter;
    private Optional<Map<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>>> allPreferencesBySearchablePreferenceScreen = Optional.empty();
    private Optional<Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity>> hostByPreference = Optional.empty();

    public SearchablePreferenceScreenDAO(final AppDatabase appDatabase) {
        this.searchablePreferenceDAO = appDatabase.searchablePreferenceDAO();
        this.daoSetter =
                new SearchablePreferenceScreenDAOSetter(
                        this,
                        new SearchablePreferenceDAOSetter(appDatabase.searchablePreferenceDAO()));
    }

    public void persist(final Collection<SearchablePreferenceScreenEntity> searchablePreferenceScreenEntities) {
        searchablePreferenceScreenEntities.forEach(this::persist);
    }

    public void persist(final SearchablePreferenceScreenEntity searchablePreferenceScreenEntity) {
        searchablePreferenceDAO.persist(searchablePreferenceScreenEntity.getAllPreferences());
        _persist(daoSetter.setDao(searchablePreferenceScreenEntity));
        invalidateCaches();
    }

    public Optional<SearchablePreferenceScreenEntity> findSearchablePreferenceScreenById(final String id) {
        return daoSetter.setDao(_findSearchablePreferenceScreenById(id));
    }

    public Set<SearchablePreferenceScreenEntity> loadAll() {
        return daoSetter.setDao(new HashSet<>(_loadAll()));
    }

    // FK-TODO: remove method?
    public Set<SearchablePreferenceScreenEntity> findSearchablePreferenceScreensByHost(final Class<? extends PreferenceFragmentCompat> host) {
        return daoSetter.setDao(new HashSet<>(_findSearchablePreferenceScreensByHost(host)));
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
    protected abstract void _persist(SearchablePreferenceScreenEntity searchablePreferenceScreenEntity);

    @Query("SELECT * FROM SearchablePreferenceScreenEntity")
    protected abstract List<SearchablePreferenceScreenEntity> _loadAll();

    @Transaction
    @Query("SELECT * FROM SearchablePreferenceScreenEntity")
    protected abstract List<SearchablePreferenceScreenAndAllPreferences> _getSearchablePreferenceScreenAndAllPreferences();

    @Query("SELECT * FROM SearchablePreferenceScreenEntity WHERE id = :id")
    protected abstract Optional<SearchablePreferenceScreenEntity> _findSearchablePreferenceScreenById(final String id);

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
                daoSetter.__setDao(
                        new HashSet<>(_getSearchablePreferenceScreenAndAllPreferences())));
    }

    private Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity> getHostByPreference() {
        if (hostByPreference.isEmpty()) {
            hostByPreference = Optional.of(computeHostByPreference());
        }
        return hostByPreference.orElseThrow();
    }

    private Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity> computeHostByPreference() {
        return SearchablePreferenceScreenAndAllPreferencesHelper.getHostByPreference(
                daoSetter.__setDao(
                        new HashSet<>(_getSearchablePreferenceScreenAndAllPreferences())));
    }

    void detachScreensFromDb(final Set<SearchablePreferenceScreenEntity> screens) {
        SearchablePreferenceScreenDAO
                .createSearchablePreferenceScreenDAOSetter(createDetachedDbDataProvider())
                .setDao(screens);
    }

    private DetachedDbDataProvider createDetachedDbDataProvider() {
        return new DetachedDbDataProvider(
                getAllPreferencesBySearchablePreferenceScreen(),
                getHostByPreference(),
                searchablePreferenceDAO.getPredecessorByPreference(),
                searchablePreferenceDAO.getChildrenByPreference());
    }

    private static SearchablePreferenceScreenDAOSetter createSearchablePreferenceScreenDAOSetter(final DetachedDbDataProvider dbDataProvider) {
        return new SearchablePreferenceScreenDAOSetter(
                dbDataProvider,
                new SearchablePreferenceDAOSetter(dbDataProvider));
    }

    private void invalidateCaches() {
        allPreferencesBySearchablePreferenceScreen = Optional.empty();
        hostByPreference = Optional.empty();
    }
}
