package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.content.Context;

import androidx.room.Room;

import java.util.Locale;
import java.util.Optional;

public class AppDatabaseFactory {

    record LocaleSpecificAppDatabase(Locale locale, AppDatabase appDatabase) {
    }

    private static volatile Optional<LocaleSpecificAppDatabase> localeSpecificAppDatabase = Optional.empty();

    public static synchronized AppDatabase getInstance(final Locale locale, final Context context) {
        localeSpecificAppDatabase =
                Optional.of(
                        getLocaleSpecificAppDatabase(
                                locale,
                                context,
                                localeSpecificAppDatabase));
        return localeSpecificAppDatabase.orElseThrow().appDatabase();
    }

    private static LocaleSpecificAppDatabase getLocaleSpecificAppDatabase(final Locale locale,
                                                                          final Context context,
                                                                          final Optional<LocaleSpecificAppDatabase> localeSpecificAppDatabase) {
        if (localeSpecificAppDatabase.isPresent()) {
            final var _localeSpecificAppDatabase = localeSpecificAppDatabase.orElseThrow();
            if (_localeSpecificAppDatabase.locale().equals(locale)) {
                return _localeSpecificAppDatabase;
            } else {
                _localeSpecificAppDatabase.appDatabase().close();
                return createLocaleSpecificAppDatabase(locale, context);
            }
        } else {
            return createLocaleSpecificAppDatabase(locale, context);
        }
    }

    private static LocaleSpecificAppDatabase createLocaleSpecificAppDatabase(final Locale locale, final Context context) {
        return new LocaleSpecificAppDatabase(
                locale,
                createInstance(locale, context));
    }

    private static AppDatabase createInstance(final Locale locale, final Context context) {
        return Room
                .databaseBuilder(
                        context,
                        AppDatabase.class,
                        "searchable_preferences_" + locale.getLanguage())
                // FK-TODO: remove allowMainThreadQueries()
                .allowMainThreadQueries()
                .build();
    }
}
