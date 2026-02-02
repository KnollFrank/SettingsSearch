package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceEntityDao;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenEntityDao;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenTreeDao;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenTreeEntityDao;
import de.KnollFrank.lib.settingssearch.db.preference.dao.TreeProcessorDescriptionEntityDao;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenTreeEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.TreeProcessorDescriptionEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.LazyPersistableBundleConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.LocaleConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.OptionalEitherIntegerOrStringConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.OptionalIntegerConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.OptionalStringConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.PersistableBundleConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.PreferenceFragmentClassConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.PreferenceFragmentClassOfActivityConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.TreeProcessorClassConverter;
import de.KnollFrank.lib.settingssearch.graph.EntityTreePojoTreeConverter;

@Database(
        entities = {
                SearchablePreferenceScreenTreeEntity.class,
                SearchablePreferenceScreenEntity.class,
                SearchablePreferenceEntity.class,
                TreeProcessorDescriptionEntity.class
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
                LazyPersistableBundleConverter.class,
                TreeProcessorClassConverter.class,
                PreferenceFragmentClassOfActivityConverter.class
        })
public abstract class PreferencesRoomDatabase extends RoomDatabase {

    private final SearchablePreferenceScreenTreeDao searchablePreferenceScreenTreeDao =
            new SearchablePreferenceScreenTreeDao(
                    new EntityTreePojoTreeConverter(),
                    searchablePreferenceScreenTreeEntityDao());

    protected PreferencesRoomDatabase() {
    }

    public SearchablePreferenceScreenTreeDao searchablePreferenceScreenTreeDao() {
        return searchablePreferenceScreenTreeDao;
    }

    public abstract SearchablePreferenceScreenTreeEntityDao searchablePreferenceScreenTreeEntityDao();

    public abstract SearchablePreferenceScreenEntityDao searchablePreferenceScreenEntityDao();

    public abstract SearchablePreferenceEntityDao searchablePreferenceEntityDao();

    public abstract TreeProcessorDescriptionEntityDao treeProcessorDescriptionEntityDao();
}
