package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;
import androidx.room.Embedded;

import de.KnollFrank.lib.settingssearch.ActivityDescription;
import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;

public record PreferenceFragmentClassOfActivitySurrogate(
        Class<? extends PreferenceFragmentCompat> preferenceFragment,
        @Embedded ActivityDescription activityOfFragment) {

    public FragmentClassOfActivity<? extends PreferenceFragmentCompat> asFragmentClassOfActivity() {
        return new FragmentClassOfActivity<>(preferenceFragment, activityOfFragment);
    }
}