package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.app.Activity;
import android.os.PersistableBundle;

public record ActivityDescription(Class<? extends Activity> activity,
                                  // FK-TODO: in der Datenbank sollte dies ein LazyPersistableBundle sein
                                  PersistableBundle arguments) {
}
