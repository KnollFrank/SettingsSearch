package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.content.Context;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.dao.LocaleDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.LocaleConverter;

class AppDatabaseProvider {

    private final LocaleDAO localeDAO;
    private final LocaleConverter localeConverter;

    public AppDatabaseProvider(final LocaleDAO localeDAO, final LocaleConverter localeConverter) {
        this.localeDAO = localeDAO;
        this.localeConverter = localeConverter;
    }

    public Set<AppDatabase> getAppDatabases(final Context context) {
        return localeConverter
                .doForward(localeDAO.getLocales())
                .stream()
                .map(locale -> AppDatabaseFactory.getInstance(locale, context))
                .collect(Collectors.toSet());
    }
}
