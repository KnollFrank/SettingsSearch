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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Optionals;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceAndChildren;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceAndChildrens;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceAndPredecessor;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceAndPredecessors;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.provider.IncludePreferenceInSearchResultsPredicate;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch;

@Dao
public abstract class SearchablePreferenceDAO implements ChildrenAndPredecessorProvider {

    private final SearchablePreferenceDAOSetter daoSetter = new SearchablePreferenceDAOSetter(this);
    private Optional<Map<SearchablePreference, SearchablePreference>> predecessorByPreference = Optional.empty();
    private Optional<Map<SearchablePreference, Set<SearchablePreference>>> childrenByPreference = Optional.empty();

    public Set<SearchablePreference> loadAll() {
        return daoSetter.setDao(new HashSet<>(_loadAll()));
    }

    public Optional<SearchablePreference> findPreferenceById(final int id) {
        return daoSetter.setDao(_findPreferenceById(id));
    }

    public Optional<SearchablePreference> findPreferenceByKeyAndHost(final String key,
                                                                     final Class<? extends PreferenceFragmentCompat> host) {
        return daoSetter.setDao(_findPreferenceByKeyAndHost(key, host));
    }

    public Set<PreferenceMatch> searchWithinTitleSummarySearchableInfo(final String needle,
                                                                       final IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate) {
        return this
                .searchWithinTitleSummarySearchableInfo(Optional.of(needle))
                .stream()
                .filter(includePreferenceInSearchResultsPredicate::includePreferenceInSearchResults)
                .map(searchablePreference -> getPreferenceMatch(searchablePreference, needle))
                .flatMap(Optionals::streamOfPresentElements)
                .collect(Collectors.toSet());
    }

    public void persist(final SearchablePreference... searchablePreferences) {
        _persist(daoSetter.setDao(searchablePreferences));
        invalidateCaches();
    }

    public void persist(final Collection<SearchablePreference> searchablePreferences) {
        _persist(daoSetter.setDao(searchablePreferences));
        invalidateCaches();
    }

    public void remove(final SearchablePreference... preferences) {
        _remove(daoSetter.setDao(preferences));
        invalidateCaches();
    }

    public void update(final SearchablePreference... preferences) {
        _update(daoSetter.setDao(preferences));
        invalidateCaches();
    }

    @Override
    public Map<SearchablePreference, SearchablePreference> getPredecessorByPreference() {
        if (predecessorByPreference.isEmpty()) {
            predecessorByPreference = Optional.of(PreferenceAndPredecessors.getPredecessorByPreference(daoSetter._setDao(_getPreferencesAndPredecessors())));
        }
        return predecessorByPreference.orElseThrow();
    }

    @Override
    public Map<SearchablePreference, Set<SearchablePreference>> getChildrenByPreference() {
        if (childrenByPreference.isEmpty()) {
            childrenByPreference = Optional.of(PreferenceAndChildrens.getChildrenByPreference(daoSetter.__setDao(_getPreferencesAndChildren())));
        }
        return childrenByPreference.orElseThrow();
    }

    public void removeAll() {
        _removeAll();
        invalidateCaches();
    }

    @Query("DELETE FROM SearchablePreference")
    protected abstract void _removeAll();

    @Query("SELECT MAX(id) FROM SearchablePreference")
    public abstract Optional<Integer> getMaxId();

    @Insert
    protected abstract void _persist(Collection<SearchablePreference> searchablePreferences);

    private static final String NEEDLE_PATTERN = "'%' || :needle || '%'";

    @Query("SELECT * FROM SearchablePreference WHERE title LIKE " + NEEDLE_PATTERN + " OR summary LIKE " + NEEDLE_PATTERN + "OR searchableInfo LIKE " + NEEDLE_PATTERN)
    protected abstract List<SearchablePreference> _searchWithinTitleSummarySearchableInfo(final Optional<String> needle);

    @Insert
    protected abstract void _persist(SearchablePreference... searchablePreferences);

    @Query("SELECT * FROM SearchablePreference WHERE `key` = :key AND host = :host")
    protected abstract Optional<SearchablePreference> _findPreferenceByKeyAndHost(String key, Class<? extends PreferenceFragmentCompat> host);

    @Query("SELECT * FROM SearchablePreference WHERE id = :id")
    protected abstract Optional<SearchablePreference> _findPreferenceById(final int id);

    @Transaction
    @Query("SELECT * FROM SearchablePreference")
    protected abstract List<PreferenceAndPredecessor> _getPreferencesAndPredecessors();

    @Query("SELECT * FROM SearchablePreference")
    protected abstract List<SearchablePreference> _loadAll();

    @Transaction
    @Query("SELECT * FROM SearchablePreference")
    protected abstract List<PreferenceAndChildren> _getPreferencesAndChildren();

    @Delete
    protected abstract void _remove(SearchablePreference... preferences);

    @Update
    protected abstract void _update(SearchablePreference... preferences);

    private Set<SearchablePreference> searchWithinTitleSummarySearchableInfo(final Optional<String> needle) {
        return daoSetter.setDao(new HashSet<>(_searchWithinTitleSummarySearchableInfo(needle)));
    }

    private void invalidateCaches() {
        predecessorByPreference = Optional.empty();
        childrenByPreference = Optional.empty();
    }
}
