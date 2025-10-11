package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

import java.util.Locale;

public interface AppDatabaseProcessor {

    void processAppDatabase(DAOProvider appDatabase, Locale locale, FragmentActivity activityContext);
}
