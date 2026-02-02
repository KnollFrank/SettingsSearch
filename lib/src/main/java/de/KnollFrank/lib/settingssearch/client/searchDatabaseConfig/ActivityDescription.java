package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.app.Activity;
import android.os.Bundle;

public record ActivityDescription(Class<? extends Activity> activity, Bundle arguments) {
}
