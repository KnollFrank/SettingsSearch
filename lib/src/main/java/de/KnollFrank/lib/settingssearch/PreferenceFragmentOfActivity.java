package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.ActivityDescription;

public record PreferenceFragmentOfActivity(
        PreferenceFragmentCompat preferenceFragment,
        ActivityDescription activityOfPreferenceFragment) {
}
