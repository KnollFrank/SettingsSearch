package de.KnollFrank.lib.settingssearch.db.preference.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

@Dao
public interface SearchablePreferencePOJODAO {

    @Query("SELECT * FROM SearchablePreferencePOJO")
    List<SearchablePreferencePOJO> getAll();

    @Insert
    void insertAll(SearchablePreferencePOJO... searchablePreferencePOJOs);
}