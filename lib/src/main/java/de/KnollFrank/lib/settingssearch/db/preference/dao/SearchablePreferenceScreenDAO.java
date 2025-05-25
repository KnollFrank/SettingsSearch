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
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenAndAllPreferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenAndAllPreferencesHelper;

@Dao
public abstract class SearchablePreferenceScreenDAO implements SearchablePreferenceScreen.DbDataProvider {

    private final SearchablePreferenceDAO searchablePreferenceDAO;
    private final SearchablePreferenceScreenDAOSetter daoSetter;
    private Optional<Map<SearchablePreferenceScreen, Set<SearchablePreference>>> allPreferencesBySearchablePreferenceScreen = Optional.empty();
    private Optional<Map<SearchablePreference, SearchablePreferenceScreen>> hostByPreference = Optional.empty();

    public SearchablePreferenceScreenDAO(final AppDatabase appDatabase) {
        this.searchablePreferenceDAO = appDatabase.searchablePreferenceDAO();
        this.daoSetter =
                new SearchablePreferenceScreenDAOSetter(
                        this,
                        new SearchablePreferenceDAOSetter(appDatabase.searchablePreferenceDAO()));
    }

    public void persist(final Collection<SearchablePreferenceScreen> searchablePreferenceScreens) {
        searchablePreferenceScreens.forEach(this::persist);
    }

    public void persist(final SearchablePreferenceScreen searchablePreferenceScreen) {
        searchablePreferenceDAO.persist(searchablePreferenceScreen.getAllPreferences());
        _persist(daoSetter.setDao(searchablePreferenceScreen));
        invalidateCaches();
    }

    public Optional<SearchablePreferenceScreen> findSearchablePreferenceScreenById(final String id) {
        return daoSetter.setDao(_findSearchablePreferenceScreenById(id));
    }

    public Set<SearchablePreferenceScreen> loadAll() {
        return daoSetter.setDao(new HashSet<>(_loadAll()));
    }

    // FK-TODO: remove method
    public Set<SearchablePreferenceScreen> findSearchablePreferenceScreensByHost(final Class<? extends PreferenceFragmentCompat> host) {
        return daoSetter.setDao(new HashSet<>(_findSearchablePreferenceScreensByHost(host)));
    }

    @Override
    public Set<SearchablePreference> getAllPreferences(final SearchablePreferenceScreen screen) {
        return Maps.get(getAllPreferencesBySearchablePreferenceScreen(), screen).orElseThrow();
    }

    @Override
    public SearchablePreferenceScreen getHost(SearchablePreference preference) {
        return Maps.get(getHostByPreference(), preference).orElseThrow();
    }

    public void removeAll() {
        searchablePreferenceDAO.removeAll();
        _removeAll();
        invalidateCaches();
    }

    @Insert
    protected abstract void _persist(SearchablePreferenceScreen searchablePreferenceScreen);

    @Query("SELECT * FROM SearchablePreferenceScreen")
    protected abstract List<SearchablePreferenceScreen> _loadAll();

    @Transaction
    @Query("SELECT * FROM SearchablePreferenceScreen")
    protected abstract List<SearchablePreferenceScreenAndAllPreferences> _getSearchablePreferenceScreenAndAllPreferences();

    @Query("SELECT * FROM SearchablePreferenceScreen WHERE id = :id")
    protected abstract Optional<SearchablePreferenceScreen> _findSearchablePreferenceScreenById(final String id);

    // FK-TODO: remove method
    @Query("SELECT * FROM SearchablePreferenceScreen WHERE host = :host")
    protected abstract List<SearchablePreferenceScreen> _findSearchablePreferenceScreensByHost(final Class<? extends PreferenceFragmentCompat> host);

    @Query("DELETE FROM SearchablePreferenceScreen")
    protected abstract void _removeAll();

    private Map<SearchablePreferenceScreen, Set<SearchablePreference>> getAllPreferencesBySearchablePreferenceScreen() {
        if (allPreferencesBySearchablePreferenceScreen.isEmpty()) {
            allPreferencesBySearchablePreferenceScreen = Optional.of(computeAllPreferencesBySearchablePreferenceScreen());
        }
        return allPreferencesBySearchablePreferenceScreen.orElseThrow();
    }

    private Map<SearchablePreferenceScreen, Set<SearchablePreference>> computeAllPreferencesBySearchablePreferenceScreen() {
        return SearchablePreferenceScreenAndAllPreferencesHelper.getAllPreferencesBySearchablePreferenceScreen(
                daoSetter.__setDao(
                        new HashSet<>(_getSearchablePreferenceScreenAndAllPreferences())));
    }

    private Map<SearchablePreference, SearchablePreferenceScreen> getHostByPreference() {
        if (hostByPreference.isEmpty()) {
            hostByPreference = Optional.of(computeHostByPreference());
        }
        return hostByPreference.orElseThrow();
    }

    private Map<SearchablePreference, SearchablePreferenceScreen> computeHostByPreference() {
        return SearchablePreferenceScreenAndAllPreferencesHelper.getHostByPreference(
                daoSetter.__setDao(
                        new HashSet<>(_getSearchablePreferenceScreenAndAllPreferences())));
    }

    void detachScreensFromDb(final Set<SearchablePreferenceScreen> screens) {
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
