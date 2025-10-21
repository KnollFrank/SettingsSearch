package de.KnollFrank.settingssearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.PersistableBundle;

import androidx.preference.PreferenceManager;

import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;

public class ConfigurationProvider {

    // FK-TODO: inline method
    public static PersistableBundle createConfiguration(final Context context) {
        return createConfiguration(PreferenceManager.getDefaultSharedPreferences(context));
    }

    private static PersistableBundle createConfiguration(final SharedPreferences preferences) {
        final PersistableBundle bundle = new PersistableBundle();
        putBooleanFromPreferencesIntoBundle(bundle, preferences, PrefsFragmentFirst.ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE_KEY);
        putBooleanFromPreferencesIntoBundle(bundle, preferences, PrefsFragmentFirst.SUMMARY_CHANGING_PREFERENCE_KEY);
        return bundle;
    }

    private static void putBooleanFromPreferencesIntoBundle(final PersistableBundle bundle, final SharedPreferences preferences, final String key) {
        bundle.putBoolean(key, preferences.getBoolean(key, false));
    }
}
