package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.ActivityDescription;

public record PreferenceScreenOfHostOfActivity(PreferenceScreen preferenceScreen,
                                               PreferenceFragmentCompat hostOfPreferenceScreen,
                                               ActivityDescription activityOfHost) {

    public FragmentClassOfActivity<? extends PreferenceFragmentCompat> asPreferenceFragmentClassOfActivity() {
        return new FragmentClassOfActivity<>(
                hostOfPreferenceScreen.getClass(),
                activityOfHost);
    }

    public PreferenceFragmentOfActivity asPreferenceFragmentOfActivity() {
        return new PreferenceFragmentOfActivity(
                hostOfPreferenceScreen,
                activityOfHost);
    }
}
