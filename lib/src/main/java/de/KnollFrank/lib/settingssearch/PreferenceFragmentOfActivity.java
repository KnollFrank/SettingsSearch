package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.ActivityDescription;

public record PreferenceFragmentOfActivity(
        Class<? extends PreferenceFragmentCompat> preferenceFragment,
        ActivityDescription activityOfPreferenceFragment) {
}
