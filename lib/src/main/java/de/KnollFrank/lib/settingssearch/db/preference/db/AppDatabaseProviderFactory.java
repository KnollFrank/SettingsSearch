package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.content.Context;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.LocaleConverter;

class AppDatabaseProviderFactory {

    public static AppDatabaseProvider createAppDatabaseProvider(final Context context) {
        return new AppDatabaseProvider(
                LocaleDatabase.getInstance(context).localeDAO(),
                new LocaleConverter());
    }
}
