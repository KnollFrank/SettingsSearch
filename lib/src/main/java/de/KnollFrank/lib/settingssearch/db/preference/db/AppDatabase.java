package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchDatabaseStateDAO;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceDAO;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenDAO;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchDatabaseState;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.BundleConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.BundleWithEqualityConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.OptionalBundleConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.OptionalBundleWithEqualityConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.OptionalEitherIntegerOrStringConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.OptionalIntegerConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.OptionalStringConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.PreferenceFragmentClassConverter;

@Database(
        entities = {
                SearchablePreferenceScreen.class,
                SearchablePreference.class,
                SearchDatabaseState.class
        },
        version = 1,
        exportSchema = false)
@TypeConverters(
        {
                PreferenceFragmentClassConverter.class,
                OptionalEitherIntegerOrStringConverter.class,
                OptionalStringConverter.class,
                OptionalIntegerConverter.class,
                BundleConverter.class,
                OptionalBundleConverter.class,
                BundleWithEqualityConverter.class,
                OptionalBundleWithEqualityConverter.class
        })
public abstract class AppDatabase extends RoomDatabase {

    protected AppDatabase() {
    }

    public abstract SearchablePreferenceScreenDAO searchablePreferenceScreenDAO();

    public abstract SearchablePreferenceDAO searchablePreferenceDAO();

    public abstract SearchDatabaseStateDAO searchDatabaseStateDAO();

    public SearchablePreferenceScreenGraphDAO searchablePreferenceScreenGraphDAO() {
        return new SearchablePreferenceScreenGraphDAO(searchablePreferenceScreenDAO());
    }
}
