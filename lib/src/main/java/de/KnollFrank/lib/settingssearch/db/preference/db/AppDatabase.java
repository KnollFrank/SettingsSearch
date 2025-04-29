package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.Locale;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchDatabaseStateDAO;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferencePOJODAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchDatabaseState;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.BundleConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.ClassConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.OptionalEitherIntegerOrStringConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.OptionalIntegerConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.OptionalStringConverter;

@Database(
        entities = {SearchablePreferencePOJO.class, SearchDatabaseState.class},
        version = 1,
        exportSchema = false)
@TypeConverters(
        {
                ClassConverter.class,
                OptionalEitherIntegerOrStringConverter.class,
                OptionalStringConverter.class,
                OptionalIntegerConverter.class,
                BundleConverter.class
        })
public abstract class AppDatabase extends RoomDatabase {

    private record LocaleSpecificAppDatabase(Locale locale, AppDatabase appDatabase) {
    }

    private static volatile Optional<LocaleSpecificAppDatabase> localeSpecificAppDatabase = Optional.empty();

    public static synchronized AppDatabase getInstance(final Context context, final Locale locale) {
        localeSpecificAppDatabase = Optional.of(getLocaleSpecificAppDatabase(context, locale, localeSpecificAppDatabase));
        return localeSpecificAppDatabase.orElseThrow().appDatabase();
    }

    private static LocaleSpecificAppDatabase getLocaleSpecificAppDatabase(final Context context,
                                                                          final Locale locale,
                                                                          final Optional<LocaleSpecificAppDatabase> localeSpecificAppDatabase) {
        if (localeSpecificAppDatabase.isPresent()) {
            final var _localeSpecificAppDatabase = localeSpecificAppDatabase.orElseThrow();
            if (_localeSpecificAppDatabase.locale.equals(locale)) {
                return _localeSpecificAppDatabase;
            } else {
                _localeSpecificAppDatabase.appDatabase().close();
                return createLocaleSpecificAppDatabase(context, locale);
            }
        } else {
            return createLocaleSpecificAppDatabase(context, locale);
        }
    }

    private static LocaleSpecificAppDatabase createLocaleSpecificAppDatabase(final Context context, final Locale locale) {
        return new LocaleSpecificAppDatabase(
                locale,
                createInstance(context, locale));
    }

    private static AppDatabase createInstance(final Context context, final Locale locale) {
        return Room
                .databaseBuilder(
                        context,
                        AppDatabase.class,
                        "searchable_preferences_" + locale.getLanguage())
                // FK-TODO: remove allowMainThreadQueries()
                .allowMainThreadQueries()
                .build();
    }

    protected AppDatabase() {
    }

    public abstract SearchablePreferencePOJODAO searchablePreferenceDAO();

    public abstract SearchDatabaseStateDAO searchDatabaseStateDAO();
}
