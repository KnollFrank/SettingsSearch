package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public record PreferenceScreenOfHostOfActivity(PreferenceScreen preferenceScreen,
                                               PreferenceFragmentCompat hostOfPreferenceScreen,
                                               ActivityDescription activityOfHost) {

    public PreferenceFragmentOfActivity asPreferenceFragmentOfActivity() {
        return new PreferenceFragmentOfActivity(
                hostOfPreferenceScreen,
                activityOfHost);
    }
}
