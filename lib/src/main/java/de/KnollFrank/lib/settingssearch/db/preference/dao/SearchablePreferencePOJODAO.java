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

    @Query("SELECT * FROM SearchablePreferencePOJO")
    public abstract List<SearchablePreferencePOJO> loadAll();

    @Query("SELECT * FROM SearchablePreferencePOJO WHERE id = :id")
    public abstract Optional<SearchablePreferencePOJO> findPreferenceById(final int id);

    @Query("SELECT * FROM SearchablePreferencePOJO WHERE `key` = :key AND host = :host")
    public abstract Optional<SearchablePreferencePOJO> findPreferenceByKeyAndHost(String key, Class<? extends PreferenceFragmentCompat> host);

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

    @Insert
    public abstract void persist(SearchablePreferencePOJO... searchablePreferencePOJOs);

    @Insert
    public abstract void persist(Collection<SearchablePreferencePOJO> searchablePreferencePOJOs);

    @Delete
    public abstract void remove(SearchablePreferencePOJO... preferences);

    @Update
    public abstract void update(SearchablePreferencePOJO... preferences);

    @Transaction
    @Query("SELECT * FROM SearchablePreferencePOJO")
    public abstract List<PreferenceAndPredecessor> getPreferencesAndPredecessors();

    @Transaction
    @Query("SELECT * FROM SearchablePreferencePOJO")
    public abstract List<PreferenceAndChildren> getPreferencesAndChildren();

    @Query("DELETE FROM SearchablePreferencePOJO")
    public abstract void removeAll();

    private static final String NEEDLE_PATTERN = "'%' || :needle || '%'";

    @Query("SELECT * FROM SearchablePreferencePOJO WHERE title LIKE " + NEEDLE_PATTERN + " OR summary LIKE " + NEEDLE_PATTERN + "OR searchableInfo LIKE " + NEEDLE_PATTERN)
    protected abstract List<SearchablePreferencePOJO> searchWithinTitleSummarySearchableInfo(final Optional<String> needle);
}
