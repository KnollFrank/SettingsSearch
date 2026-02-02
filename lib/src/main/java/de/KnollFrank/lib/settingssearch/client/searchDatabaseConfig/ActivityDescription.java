package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.app.Activity;
import android.os.PersistableBundle;

public record ActivityDescription(Class<? extends Activity> activity,
                                  PersistableBundle arguments) {
}
