package de.KnollFrank.lib.settingssearch.db.preference.db;

import androidx.fragment.app.FragmentActivity;

public interface AppDatabaseProcessor {

    void processAppDatabase(DAOProvider appDatabase, FragmentActivity activityContext);
}
