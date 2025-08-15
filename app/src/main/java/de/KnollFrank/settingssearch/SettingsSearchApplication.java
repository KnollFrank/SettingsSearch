package de.KnollFrank.settingssearch;

import android.app.Application;
import android.content.Context;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.db.AppDatabaseConfig;
import de.KnollFrank.lib.settingssearch.db.preference.db.DAOProvider;
import de.KnollFrank.lib.settingssearch.db.preference.db.DAOProviderFactory;

public class SettingsSearchApplication extends Application {

    private Optional<DAOProvider> daoProvider = Optional.empty();
    private static final Object LOCK = new Object();

    public static SettingsSearchApplication getInstanceFromContext(final Context context) {
        return (SettingsSearchApplication) context.getApplicationContext();
    }

    public DAOProvider getDAOProvider() {
        if (daoProvider.isEmpty()) {
            synchronized (LOCK) {
                if (daoProvider.isEmpty()) {
                    daoProvider = Optional.of(creatDAOProvider());
                }
            }
        }
        return daoProvider.orElseThrow();
    }

    private DAOProvider creatDAOProvider() {
        return DAOProviderFactory.createDAOProvider(
                new AppDatabaseConfig(
                        "settings_search.db",
                        Optional.empty(),
                        AppDatabaseConfig.JournalMode.WRITE_AHEAD_LOGGING),
                this);
    }
}
