package de.KnollFrank.settingssearch;

import android.app.Application;
import android.content.Context;

import de.KnollFrank.lib.settingssearch.db.preference.db.DAOProviderManager;

public class SettingsSearchApplication extends Application {

    public final DAOProviderManager<Configuration> daoProviderManager = new DAOProviderManager<>();

    public static SettingsSearchApplication getInstanceFromContext(final Context context) {
        return (SettingsSearchApplication) context.getApplicationContext();
    }
}
