package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.ActivityDescription;

public record PreferenceFragmentClassOfActivity(
        Class<? extends PreferenceFragmentCompat> preferenceFragmentClass,
        ActivityDescription activityOfPreferenceFragment) {

    public FragmentClassOfActivity asFragmentClassOfActivity() {
        return new FragmentClassOfActivity(preferenceFragmentClass, activityOfPreferenceFragment);
    }
}
