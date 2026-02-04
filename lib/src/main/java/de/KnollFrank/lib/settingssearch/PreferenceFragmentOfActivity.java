package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;

public record PreferenceFragmentOfActivity(
        PreferenceFragmentCompat preferenceFragment,
        ActivityDescription activityOfPreferenceFragment) {

    public FragmentClassOfActivity<? extends PreferenceFragmentCompat> asPreferenceFragmentClassOfActivity() {
        return new FragmentClassOfActivity<>(
                preferenceFragment.getClass(),
                activityOfPreferenceFragment);
    }
}
