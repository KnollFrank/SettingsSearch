package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.app.Activity;

import androidx.preference.PreferenceFragmentCompat;

public record ActivityWithRootPreferenceFragment<A extends Activity, P extends PreferenceFragmentCompat>(
        Class<A> activityClass,
        Class<P> rootPreferenceFragmentClass) {
}
