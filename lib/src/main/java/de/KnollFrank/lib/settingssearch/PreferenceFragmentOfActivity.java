package de.KnollFrank.lib.settingssearch;

import android.app.Activity;

import androidx.preference.PreferenceFragmentCompat;

public record PreferenceFragmentOfActivity(
        Class<? extends PreferenceFragmentCompat> preferenceFragment,
        Class<? extends Activity> activityOfPreferenceFragment) {
}
