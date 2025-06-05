package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchDatabaseStateDAO;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceEntityDAO;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenEntityDAO;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchDatabaseState;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.OptionalEitherIntegerOrStringConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.OptionalIntegerConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.OptionalStringConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.PreferenceFragmentClassConverter;

@Database(
        entities = {
                SearchablePreferenceScreenEntity.class,
                SearchablePreferenceEntity.class,
                SearchDatabaseState.class
        },
        version = 1,
        exportSchema = false)
@TypeConverters(
        {
                PreferenceFragmentClassConverter.class,
                OptionalEitherIntegerOrStringConverter.class,
                OptionalStringConverter.class,
                OptionalIntegerConverter.class
        })
public abstract class AppDatabase extends RoomDatabase implements DAOProvider {

    protected AppDatabase() {
    }

    @Override
    public SearchablePreferenceScreenGraphDAO searchablePreferenceScreenGraphDAO() {
        return new SearchablePreferenceScreenGraphDAO(searchablePreferenceScreenEntityDAO(), searchablePreferenceEntityDAO());
    }

    @Override
    public abstract SearchablePreferenceScreenEntityDAO searchablePreferenceScreenEntityDAO();

    @Override
    public abstract SearchablePreferenceEntityDAO searchablePreferenceEntityDAO();

    public abstract SearchDatabaseStateDAO searchDatabaseStateDAO();
}
