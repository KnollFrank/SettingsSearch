package de.KnollFrank.lib.settingssearch.db.preference.dao;

import androidx.preference.PreferenceFragmentCompat;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.google.common.collect.Iterables;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabase;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.BundleWithEquality;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.HostWithArguments;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenAndAllPreferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenAndAllPreferencesHelper;

@Dao
public abstract class SearchablePreferenceScreenDAO implements AllPreferencesProvider {

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

    public Optional<SearchablePreferenceScreen> findSearchablePreferenceScreenById(final int id) {
        return daoSetter.setDao(_findSearchablePreferenceScreenById(id));
    }

    public Set<SearchablePreferenceScreen> loadAll() {
        return daoSetter.setDao(new HashSet<>(_loadAll()));
    }

    // FK-TODO: unit test this method
    public SearchablePreferenceScreen findSearchablePreferenceScreenByHostWithArguments(final HostWithArguments hostWithArguments) {
        return daoSetter.setDao(
                Iterables.getOnlyElement(
                        findSearchablePreferenceScreenByHostWithArguments(
                                hostWithArguments.host(),
                                hostWithArguments.arguments())));
    }

    @Override
    public Map<SearchablePreferenceScreen, Set<SearchablePreference>> getAllPreferencesBySearchablePreferenceScreen() {
        if (allPreferencesBySearchablePreferenceScreen.isEmpty()) {
            allPreferencesBySearchablePreferenceScreen =
                    Optional.of(
                            SearchablePreferenceScreenAndAllPreferencesHelper.getAllPreferencesBySearchablePreferenceScreen(
                                    daoSetter.__setDao(
                                            new HashSet<>(_getSearchablePreferenceScreenAndAllPreferences()))));
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

    @Query("SELECT * FROM SearchablePreferenceScreen WHERE id = :id")
    protected abstract Optional<SearchablePreferenceScreen> _findSearchablePreferenceScreenById(final int id);

    @Query("SELECT * FROM SearchablePreferenceScreen WHERE host = :host AND arguments = :arguments")
    // FK-TODO: return Optional<SearchablePreferenceScreen> because of unique constraint
    protected abstract List<SearchablePreferenceScreen> findSearchablePreferenceScreenByHostWithArguments(
            final Class<? extends PreferenceFragmentCompat> host,
            final Optional<BundleWithEquality> arguments);

    private void invalidateCaches() {
        allPreferencesBySearchablePreferenceScreen = Optional.empty();
    }
}
