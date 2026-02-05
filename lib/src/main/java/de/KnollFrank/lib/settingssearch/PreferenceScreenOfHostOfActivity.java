package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public record PreferenceScreenOfHostOfActivity(PreferenceScreen preferenceScreen,
                                               PreferenceFragmentCompat hostOfPreferenceScreen,
                                               ActivityDescription activityOfHost) {

    public FragmentOfActivity<? extends PreferenceFragmentCompat> asPreferenceFragmentOfActivity() {
        return new FragmentOfActivity<>(hostOfPreferenceScreen, activityOfHost);
    }
}
