package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public record PreferenceOfHostOfActivity(Preference preference,
                                         PreferenceFragmentCompat hostOfPreference,
                                         ActivityDescription activityOfHost) {

    public FragmentOfActivity<? extends PreferenceFragmentCompat> asPreferenceFragmentOfActivity() {
        return new FragmentOfActivity<>(hostOfPreference, activityOfHost);
    }
}
