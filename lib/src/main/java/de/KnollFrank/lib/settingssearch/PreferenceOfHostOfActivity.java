package de.KnollFrank.lib.settingssearch;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;

public record PreferenceOfHostOfActivity(Preference preference,
                                         Fragment hostOfPreference,
                                         ActivityDescription activityOfHost) {

    public FragmentOfActivity<? extends Fragment> asPreferenceFragmentOfActivity() {
        return new FragmentOfActivity<>(hostOfPreference, activityOfHost);
    }
}
