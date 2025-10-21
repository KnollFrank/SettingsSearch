package de.KnollFrank.settingssearch;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;

public class ConfigurationFactory {

    public static Configuration createConfiguration(final Context context) {
        return createConfiguration(PreferenceManager.getDefaultSharedPreferences(context));
    }

    private static Configuration createConfiguration(final SharedPreferences preferences) {
        return new Configuration(
                preferences.getBoolean(PrefsFragmentFirst.ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE_KEY, false),
                preferences.getBoolean(PrefsFragmentFirst.SUMMARY_CHANGING_PREFERENCE_KEY, false));
    }
}
