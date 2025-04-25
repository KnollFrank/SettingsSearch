package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

// FK-TODO: move to package de.KnollFrank.lib.settingssearch.db.preference.dao?
@Dao
public abstract class SearchablePreferencePOJODAO {

    @Query("SELECT * FROM SearchablePreferencePOJO")
    public abstract List<SearchablePreferencePOJO> loadAll();

    @Query("SELECT * FROM SearchablePreferencePOJO WHERE id = :id")
    public abstract Optional<SearchablePreferencePOJO> findPreferenceById(final int id);

    @Query("SELECT * FROM SearchablePreferencePOJO WHERE `key` = :key AND host = :host")
    public abstract Optional<SearchablePreferencePOJO> findPreferenceByKeyAndHost(String key, Class<? extends PreferenceFragmentCompat> host);

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
}
