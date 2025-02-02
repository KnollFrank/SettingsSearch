package de.KnollFrank.lib.settingssearch.provider;

import android.app.Activity;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

@FunctionalInterface
public interface RootPreferenceFragmentOfActivityProvider {

    Optional<Class<? extends PreferenceFragmentCompat>> getRootPreferenceFragmentOfActivity(Class<? extends Activity> activityClass);
}
