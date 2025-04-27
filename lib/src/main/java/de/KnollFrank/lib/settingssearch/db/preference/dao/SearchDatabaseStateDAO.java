package de.KnollFrank.lib.settingssearch.db.preference.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchDatabaseState;

@Dao
public abstract class SearchDatabaseStateDAO {

    public void setSearchDatabaseInitialized(final boolean initialized) {
        final SearchDatabaseState searchDatabaseState = getSearchDatabaseState();
        searchDatabaseState.setInitialized(initialized);
        update(searchDatabaseState);
    }

    public boolean isSearchDatabaseInitialized() {
        return getSearchDatabaseState().isInitialized();
    }

    @Query("SELECT * FROM SearchDatabaseState")
    protected abstract Optional<SearchDatabaseState> _getSearchDatabaseState();

    @Insert
    protected abstract void persist(SearchDatabaseState searchDatabaseState);

    @Update
    protected abstract void update(SearchDatabaseState searchDatabaseState);

    private SearchDatabaseState getSearchDatabaseState() {
        return this
                ._getSearchDatabaseState()
                .orElseGet(() -> {
                    final SearchDatabaseState searchDatabaseState = new SearchDatabaseState(1, false);
                    persist(searchDatabaseState);
                    return searchDatabaseState;
                });
    }
}
