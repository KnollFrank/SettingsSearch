package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceEntityDAO;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenEntityDAO;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphEntityDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraphEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.LocaleConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.OptionalEitherIntegerOrStringConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.OptionalIntegerConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.OptionalStringConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.PersistableBundleConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.PreferenceFragmentClassConverter;
import de.KnollFrank.lib.settingssearch.graph.EntityGraphPojoGraphConverter;

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
                PersistableBundleConverter.class
        })
public abstract class PreferencesRoomDatabase extends RoomDatabase {

    // FK-TODO: move to PreferencesDatabaseFactory
    private Optional<SearchablePreferenceScreenGraphDAO> screenGraphDAO = Optional.empty();

    protected PreferencesRoomDatabase() {
    }

    public SearchablePreferenceScreenGraphDAO searchablePreferenceScreenGraphDAO() {
        if (screenGraphDAO.isEmpty()) {
            screenGraphDAO = Optional.of(createSearchablePreferenceScreenGraphDAO());
        }
        return screenGraphDAO.orElseThrow();
    }

    public abstract SearchablePreferenceScreenGraphEntityDAO searchablePreferenceScreenGraphEntityDAO();

    public abstract SearchablePreferenceScreenEntityDAO searchablePreferenceScreenEntityDAO();

    public abstract SearchablePreferenceEntityDAO searchablePreferenceEntityDAO();

    private SearchablePreferenceScreenGraphDAO createSearchablePreferenceScreenGraphDAO() {
        return new SearchablePreferenceScreenGraphDAO(
                new EntityGraphPojoGraphConverter(),
                searchablePreferenceScreenGraphEntityDAO());
    }
}
