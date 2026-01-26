package de.KnollFrank.lib.settingssearch.db.preference.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.TreeProcessorDescriptionEntity;

@Dao
// FK-TODO: add unit test
public interface TreeProcessorDescriptionEntityDao {

    @Query("SELECT * FROM TreeProcessorDescriptionEntity")
    List<TreeProcessorDescriptionEntity> getAll();

    // FK-TODO: remove ... notation
    @Insert
    void insertAll(TreeProcessorDescriptionEntity... treeProcessorDescriptionEntities);

    @Query("DELETE FROM TreeProcessorDescriptionEntity")
    void deleteAll();
}
