package de.KnollFrank.lib.settingssearch.db.preference.dao;

import androidx.preference.PreferenceFragmentCompat;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceAndChildren;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceAndPredecessor;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.provider.IncludeSearchablePreferencePOJOInSearchResultsPredicate;

@Dao
public abstract class SearchablePreferencePOJODAO {

    @Query("SELECT * FROM SearchablePreferencePOJO")
    public abstract List<SearchablePreferencePOJO> loadAll();

    @Query("SELECT * FROM SearchablePreferencePOJO WHERE id = :id")
    public abstract Optional<SearchablePreferencePOJO> findPreferenceById(final int id);

    @Query("SELECT * FROM SearchablePreferencePOJO WHERE `key` = :key AND host = :host")
    public abstract Optional<SearchablePreferencePOJO> findPreferenceByKeyAndHost(String key, Class<? extends PreferenceFragmentCompat> host);

    public List<SearchablePreferencePOJO> searchWithinTitleSummarySearchableInfo(
            final String needle,
            final IncludeSearchablePreferencePOJOInSearchResultsPredicate includePreferenceInSearchResultsPredicate) {
        return this
                .searchWithinTitleSummarySearchableInfo(Optional.of(needle))
                .stream()
                .filter(includePreferenceInSearchResultsPredicate::includePreferenceInSearchResults)
                .collect(Collectors.toList());
    }

    @Insert
    public abstract void persist(SearchablePreferencePOJO... searchablePreferencePOJOs);

    @Insert
    public abstract void persist(Collection<SearchablePreferencePOJO> searchablePreferencePOJOs);

    @Delete
    public abstract void remove(SearchablePreferencePOJO... preferences);

    @Transaction
    @Query("SELECT * FROM SearchablePreferencePOJO")
    public abstract List<PreferenceAndPredecessor> getPreferencesAndPredecessors();

    @Transaction
    @Query("SELECT * FROM SearchablePreferencePOJO")
    public abstract List<PreferenceAndChildren> getPreferencesAndChildren();

    @Query("DELETE FROM SearchablePreferencePOJO")
    public abstract void removeAll();


    private static final String pattern = "'%' || :needle || '%'";

    @Query("SELECT * FROM SearchablePreferencePOJO WHERE title LIKE " + pattern + " OR summary LIKE " + pattern + "OR searchableInfo LIKE " + pattern)
    protected abstract List<SearchablePreferencePOJO> searchWithinTitleSummarySearchableInfo(final Optional<String> needle);
}
