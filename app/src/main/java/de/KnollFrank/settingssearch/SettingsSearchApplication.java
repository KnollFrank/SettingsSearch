package de.KnollFrank.settingssearch;

import android.app.Application;
import android.content.Context;

import de.KnollFrank.lib.settingssearch.db.preference.db.PreferencesDatabaseManager;
import de.KnollFrank.lib.settingssearch.fragment.Activities;

public class SettingsSearchApplication extends Application {

    public final PreferencesDatabaseManager<Configuration> preferencesDatabaseManager = new PreferencesDatabaseManager<>();

    public static SettingsSearchApplication getInstanceFromContext(final Context context) {
        return (SettingsSearchApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Activities.initialize(this);
    }
}
