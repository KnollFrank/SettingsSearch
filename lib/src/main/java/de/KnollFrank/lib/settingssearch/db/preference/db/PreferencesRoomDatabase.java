package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceEntityDAO;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenEntityDAO;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphEntityDAO;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenTreeDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.LazyPersistableBundleConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.LocaleConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.OptionalEitherIntegerOrStringConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.OptionalIntegerConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.OptionalStringConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.PersistableBundleConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.PreferenceFragmentClassConverter;
import de.KnollFrank.lib.settingssearch.graph.EntityTreePojoTreeConverter;

@Database(
        entities = {
                SearchablePreferenceScreenGraphEntity.class,
                SearchablePreferenceScreenEntity.class,
                SearchablePreferenceEntity.class
        },
        version = 1,
        exportSchema = false)
@TypeConverters(
        {
                PreferenceFragmentClassConverter.class,
                OptionalEitherIntegerOrStringConverter.class,
                OptionalStringConverter.class,
                OptionalIntegerConverter.class,
                LocaleConverter.class,
                PersistableBundleConverter.class,
                LazyPersistableBundleConverter.class
        })
public abstract class PreferencesRoomDatabase extends RoomDatabase {

    private final SearchablePreferenceScreenTreeDAO searchablePreferenceScreenTreeDAO =
            new SearchablePreferenceScreenTreeDAO(
                    new EntityTreePojoTreeConverter(),
                    searchablePreferenceScreenGraphEntityDAO());

    protected PreferencesRoomDatabase() {
    }

    public SearchablePreferenceScreenTreeDAO searchablePreferenceScreenGraphDAO() {
        return searchablePreferenceScreenTreeDAO;
    }

    public abstract SearchablePreferenceScreenGraphEntityDAO searchablePreferenceScreenGraphEntityDAO();

    public abstract SearchablePreferenceScreenEntityDAO searchablePreferenceScreenEntityDAO();

    public abstract SearchablePreferenceEntityDAO searchablePreferenceEntityDAO();
}
