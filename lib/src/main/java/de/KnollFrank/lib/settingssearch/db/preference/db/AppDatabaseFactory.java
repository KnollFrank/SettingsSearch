package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.content.Context;

import androidx.room.Room;

import java.util.Locale;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Utils;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.LocaleConverter;

// FK-TODO: use a single AppDatabase for all locales
public class AppDatabaseFactory {

    record LocaleSpecificAppDatabase(Locale locale, AppDatabase appDatabase) {
    }

    private static volatile Optional<LocaleSpecificAppDatabase> localeSpecificAppDatabase = Optional.empty();

    public static synchronized AppDatabase getInstance(final Locale locale, final Context context) {
        setLocaleSpecificAppDatabase(locale, context);
        return localeSpecificAppDatabase.orElseThrow().appDatabase();
    }

    public static AppDatabase getInstanceForCurrentLocale(final Context context) {
        return getInstance(Utils.geCurrentLocale(context.getResources()), context);
    }

    private static void setLocaleSpecificAppDatabase(final Locale locale, final Context context) {
        localeSpecificAppDatabase.ifPresentOrElse(
                _localeSpecificAppDatabase -> {
                    if (!_localeSpecificAppDatabase.locale().equals(locale)) {
                        localeSpecificAppDatabase = Optional.of(createLocaleSpecificAppDatabase(locale, context));
                    }
                },
                () -> localeSpecificAppDatabase = Optional.of(createLocaleSpecificAppDatabase(locale, context)));
    }

    private static LocaleSpecificAppDatabase createLocaleSpecificAppDatabase(final Locale locale, final Context context) {
        return new LocaleSpecificAppDatabase(
                locale,
                createInstance(locale, context));
    }

    private static AppDatabase createInstance(final Locale locale, final Context context) {
        rememberCreationOfAppDatabaseForLocale(locale, context);
        return Room
                .databaseBuilder(
                        context,
                        AppDatabase.class,
                        "searchable_preferences_" + locale.getLanguage())
                // FK-TODO: remove allowMainThreadQueries()
                .allowMainThreadQueries()
                .build();
    }

    private static void rememberCreationOfAppDatabaseForLocale(final Locale locale, final Context context) {
        LocaleDatabase
                .getInstance(context)
                .localeDAO()
                .persist(new LocaleConverter().doBackward(locale));
    }
}
