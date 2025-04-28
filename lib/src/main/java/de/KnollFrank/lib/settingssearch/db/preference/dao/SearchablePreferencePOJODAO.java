package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static de.KnollFrank.lib.settingssearch.search.PreferencePOJOMatcher.getPreferenceMatch;

import androidx.preference.PreferenceFragmentCompat;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Optionals;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceAndChildren;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceAndPredecessor;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.provider.IncludeSearchablePreferencePOJOInSearchResultsPredicate;
import de.KnollFrank.lib.settingssearch.search.SearchablePreferencePOJOMatch;

@Dao
public abstract class SearchablePreferencePOJODAO {

    private final SearchablePreferencePOJODAOSetter daoSetter = new SearchablePreferencePOJODAOSetter(this);

    public List<SearchablePreferencePOJO> loadAll() {
        return daoSetter.setDao(_loadAll());
    }

    public Optional<SearchablePreferencePOJO> findPreferenceById(final int id) {
        return daoSetter.setDao(_findPreferenceById(id));
    }

    public Optional<SearchablePreferencePOJO> findPreferenceByKeyAndHost(final String key,
                                                                         final Class<? extends PreferenceFragmentCompat> host) {
        return daoSetter.setDao(_findPreferenceByKeyAndHost(key, host));
    }

    public Set<SearchablePreferencePOJOMatch> searchWithinTitleSummarySearchableInfo(
            final String needle,
            final IncludeSearchablePreferencePOJOInSearchResultsPredicate includePreferenceInSearchResultsPredicate) {
        return this
                .searchWithinTitleSummarySearchableInfo(Optional.of(needle))
                .stream()
                .filter(includePreferenceInSearchResultsPredicate::includePreferenceInSearchResults)
                .map(searchablePreference -> getPreferenceMatch(searchablePreference, needle))
                .flatMap(Optionals::streamOfPresentElements)
                .collect(Collectors.toSet());
    }

    public void persist(final SearchablePreferencePOJO... searchablePreferencePOJOs) {
        _persist(daoSetter.setDao(searchablePreferencePOJOs));
    }

    public void persist(final Collection<SearchablePreferencePOJO> searchablePreferencePOJOs) {
        _persist(daoSetter.setDao(searchablePreferencePOJOs));
    }

    public void remove(final SearchablePreferencePOJO... preferences) {
        _remove(daoSetter.setDao(preferences));
    }

    public void update(final SearchablePreferencePOJO... preferences) {
        _update(daoSetter.setDao(preferences));
    }

    public List<PreferenceAndPredecessor> getPreferencesAndPredecessors() {
        return daoSetter._setDao(_getPreferencesAndPredecessors());
    }

    public List<PreferenceAndChildren> getPreferencesAndChildren() {
        return daoSetter.__setDao(_getPreferencesAndChildren());
    }

    @Query("DELETE FROM SearchablePreferencePOJO")
    public abstract void removeAll();

    @Query("SELECT MAX(id) FROM SearchablePreferencePOJO")
    public abstract Optional<Integer> getMaxId();

    @Insert
    protected abstract void _persist(Collection<SearchablePreferencePOJO> searchablePreferencePOJOs);

    private static final String NEEDLE_PATTERN = "'%' || :needle || '%'";

    @Query("SELECT * FROM SearchablePreferencePOJO WHERE title LIKE " + NEEDLE_PATTERN + " OR summary LIKE " + NEEDLE_PATTERN + "OR searchableInfo LIKE " + NEEDLE_PATTERN)
    protected abstract List<SearchablePreferencePOJO> _searchWithinTitleSummarySearchableInfo(final Optional<String> needle);

    @Insert
    protected abstract void _persist(SearchablePreferencePOJO... searchablePreferencePOJOs);

    @Query("SELECT * FROM SearchablePreferencePOJO WHERE `key` = :key AND host = :host")
    protected abstract Optional<SearchablePreferencePOJO> _findPreferenceByKeyAndHost(String key, Class<? extends PreferenceFragmentCompat> host);

    @Query("SELECT * FROM SearchablePreferencePOJO WHERE id = :id")
    protected abstract Optional<SearchablePreferencePOJO> _findPreferenceById(final int id);

    @Transaction
    @Query("SELECT * FROM SearchablePreferencePOJO")
    protected abstract List<PreferenceAndPredecessor> _getPreferencesAndPredecessors();

    @Query("SELECT * FROM SearchablePreferencePOJO")
    protected abstract List<SearchablePreferencePOJO> _loadAll();

    @Transaction
    @Query("SELECT * FROM SearchablePreferencePOJO")
    protected abstract List<PreferenceAndChildren> _getPreferencesAndChildren();

    @Delete
    protected abstract void _remove(SearchablePreferencePOJO... preferences);

    @Update
    protected abstract void _update(SearchablePreferencePOJO... preferences);

    private List<SearchablePreferencePOJO> searchWithinTitleSummarySearchableInfo(final Optional<String> needle) {
        return daoSetter.setDao(_searchWithinTitleSummarySearchableInfo(needle));
    }
}
