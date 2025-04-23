package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.google.common.collect.MoreCollectors;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Dao
public abstract class SearchablePreferencePOJODAO {

    @Query("SELECT * FROM SearchablePreferencePOJO")
    public abstract List<SearchablePreferencePOJO> loadAll();

    @Query("SELECT * FROM SearchablePreferencePOJO WHERE id = :id")
    protected abstract List<SearchablePreferencePOJO> _findPreferenceById(int id);

    public Optional<SearchablePreferencePOJO> findPreferenceById(final int id) {
        return this
                ._findPreferenceById(id)
                .stream()
                .collect(MoreCollectors.toOptional());
    }

    @Insert
    public abstract void persist(SearchablePreferencePOJO... searchablePreferencePOJOs);

    @Insert
    public abstract void persist(Collection<SearchablePreferencePOJO> searchablePreferencePOJOs);

    @Delete
    public abstract void remove(SearchablePreferencePOJO... preferences);

    @Query("DELETE FROM SearchablePreferencePOJO")
    public abstract void removeAll();
}
