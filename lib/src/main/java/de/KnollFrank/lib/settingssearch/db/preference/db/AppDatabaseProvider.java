package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.content.Context;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.Locale;

class AppDatabaseProvider {

    public static Set<AppDatabase> getAppDatabases(final Context context) {
        final Set<Locale> locales = LocaleDatabase.getInstance(context).localeDAO().getLocales();
        return asStandardLocales(locales)
                .stream()
                .map(locale -> AppDatabaseFactory.getInstance(locale, context))
                .collect(Collectors.toSet());
    }

    private static Set<java.util.Locale> asStandardLocales(final Set<Locale> locales) {
        return locales
                .stream()
                .map(AppDatabaseProvider::asStandardLocale)
                .collect(Collectors.toSet());
    }

    // FK-TODO: there are multiple conversions from java.util.Locale to de.KnollFrank.lib.settingssearch.db.preference.pojo.Locale and back. Introduce converter class.
    private static java.util.Locale asStandardLocale(final Locale locale) {
        return new java.util.Locale(locale.languageCode());
    }
}
