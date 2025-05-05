package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.content.Context;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.Locale;

public class DatabasesResetter {

    public static void resetDatabases(final Context context) {
        resetDatabases(getAppDatabases(context));
    }

    public static void resetDatabases(final Collection<AppDatabase> appDatabases) {
        appDatabases.forEach(DatabasesResetter::resetDatabase);
    }

    public static void resetDatabase(final AppDatabase appDatabase) {
        appDatabase.searchablePreferenceDAO().removeAll();
        appDatabase.searchDatabaseStateDAO().setSearchDatabaseInitialized(false);
    }

    // FK-TODO: extract to separate class
    private static Set<AppDatabase> getAppDatabases(final Context context) {
        final Set<Locale> locales = LocaleDatabase.getInstance(context).localeDAO().getLocales();
        return DatabasesResetter
                .asStandardLocales(locales)
                .stream()
                .map(locale -> AppDatabaseFactory.getInstance(locale, context))
                .collect(Collectors.toSet());
    }

    private static Set<java.util.Locale> asStandardLocales(final Set<Locale> locales) {
        return locales
                .stream()
                .map(DatabasesResetter::asStandardLocale)
                .collect(Collectors.toSet());
    }

    // FK-TODO: there are multiple conversions from java.util.Locale to de.KnollFrank.lib.settingssearch.db.preference.pojo.Locale and back. Introduce converter class.
    private static java.util.Locale asStandardLocale(final Locale locale) {
        return new java.util.Locale(locale.languageCode());
    }
}
