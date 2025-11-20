package de.KnollFrank.settingssearch;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFifth;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;

public class ConfigurationProvider {

    public static Configuration getActualConfiguration(final Context context) {
        return getConfiguration(PreferenceManager.getDefaultSharedPreferences(context));
    }

    private static Configuration getConfiguration(final SharedPreferences preferences) {
        return new Configuration(
                preferences.getBoolean(PrefsFragmentFifth.ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE_KEY, false),
                preferences.getBoolean(PrefsFragmentFirst.SUMMARY_CHANGING_PREFERENCE_KEY, false));
    }
}
