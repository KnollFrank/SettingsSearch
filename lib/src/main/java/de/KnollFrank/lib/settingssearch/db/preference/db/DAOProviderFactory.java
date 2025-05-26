package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.content.Context;

import java.util.Locale;

public class DAOProviderFactory {

    public static synchronized DAOProvider getDAOProvider(final Locale locale, final Context context) {
        return AppDatabaseFactory.getInstance(locale, context);
    }
}
