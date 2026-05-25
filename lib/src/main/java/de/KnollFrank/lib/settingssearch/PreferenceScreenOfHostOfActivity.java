package de.KnollFrank.lib.settingssearch;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;

import java.util.List;
import java.util.Optional;

public record PreferenceScreenOfHostOfActivity(List<Preference> preferences,
                                               Optional<String> title,
                                               Optional<String> summary,
                                               Fragment hostOfPreferenceScreen,
                                               ActivityDescription activityOfHost) {

    public FragmentOfActivity<? extends Fragment> asPreferenceFragmentOfActivity() {
        return new FragmentOfActivity<>(hostOfPreferenceScreen, activityOfHost);
    }
}
