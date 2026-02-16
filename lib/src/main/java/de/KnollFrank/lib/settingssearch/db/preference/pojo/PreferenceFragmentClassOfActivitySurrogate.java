package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;
import androidx.room.Embedded;

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;

public record PreferenceFragmentClassOfActivitySurrogate(
        Class<? extends PreferenceFragmentCompat> preferenceFragment,
        @Embedded ActivityDescriptionSurrogate activityOfFragment) {

    public FragmentClassOfActivity<? extends PreferenceFragmentCompat> asFragmentClassOfActivity() {
        return new FragmentClassOfActivity<>(
                preferenceFragment,
                activityOfFragment.asActivityDescription());
    }
}