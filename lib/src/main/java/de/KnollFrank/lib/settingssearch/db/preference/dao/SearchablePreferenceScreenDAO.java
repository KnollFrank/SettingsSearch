package de.KnollFrank.lib.settingssearch.db.preference.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenAndAllPreferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenAndAllPreferencesHelper;

@Dao
public abstract class SearchablePreferenceScreenDAO implements AllPreferencesBySearchablePreferenceScreenProvider {

    private final SearchablePreferenceDAO searchablePreferenceDAO;
    private final SearchablePreferenceScreenDAOSetter daoSetter;
    private Optional<Map<SearchablePreferenceScreen, Set<SearchablePreference>>> allPreferencesBySearchablePreferenceScreen = Optional.empty();

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
        _persist(daoSetter.setDao(searchablePreferenceScreen));
        searchablePreferenceDAO.persist(searchablePreferenceScreen.getAllPreferences());
        invalidateCaches();
    }

    public List<SearchablePreferenceScreen> loadAll() {
        return daoSetter.setDao(_loadAll());
    }

    @Override
    public Map<SearchablePreferenceScreen, Set<SearchablePreference>> getAllPreferencesBySearchablePreferenceScreen() {
        if (allPreferencesBySearchablePreferenceScreen.isEmpty()) {
            allPreferencesBySearchablePreferenceScreen =
                    Optional.of(
                            SearchablePreferenceScreenAndAllPreferencesHelper.getAllPreferencesBySearchablePreferenceScreen(
                                    daoSetter.__setDao(
                                            _getSearchablePreferenceScreenAndAllPreferences())));
        }
        return allPreferencesBySearchablePreferenceScreen.orElseThrow();
    }

    @Insert
    protected abstract void _persist(SearchablePreferenceScreen searchablePreferenceScreen);

    @Query("SELECT * FROM SearchablePreferenceScreen")
    protected abstract List<SearchablePreferenceScreen> _loadAll();

    @Transaction
    @Query("SELECT * FROM SearchablePreferenceScreen")
    protected abstract List<SearchablePreferenceScreenAndAllPreferences> _getSearchablePreferenceScreenAndAllPreferences();

    private void invalidateCaches() {
        allPreferencesBySearchablePreferenceScreen = Optional.empty();
    }
}
