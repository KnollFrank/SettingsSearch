package de.KnollFrank.settingssearch.preference.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public class PreferenceFragmentWithSinglePreference extends PreferenceFragmentCompat {

    public static final String TITLE_OF_DST_PREFERENCE_COMING_FROM_SRC_WITH_EXTRAS = "title of dst preference coming from src with extras";
    public static final String TITLE_OF_DST_PREFERENCE_COMING_FROM_SRC_WITHOUT_EXTRAS = "title of dst preference coming from src without extras";

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        final Context context = getPreferenceManager().getContext();
        final PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);
        final Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.containsKey(PrefsFragmentFirst.BUNDLE_KEY_OF_SUMMARY_OF_SRC_PREFERENCE_WITH_EXTRAS)) {
                screen.addPreference(
                        createPreference(
                                "keyOfPreferenceOfConnectedFragment1",
                                TITLE_OF_DST_PREFERENCE_COMING_FROM_SRC_WITH_EXTRAS,
                                arguments.getString(PrefsFragmentFirst.BUNDLE_KEY_OF_SUMMARY_OF_SRC_PREFERENCE_WITH_EXTRAS),
                                context));
            }
            if (arguments.containsKey(PrefsFragmentFirst.BUNDLE_KEY_OF_SUMMARY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS)) {
                screen.addPreference(
                        createPreference("keyOfPreferenceOfConnectedFragment2",
                                TITLE_OF_DST_PREFERENCE_COMING_FROM_SRC_WITHOUT_EXTRAS,
                                arguments.getString(PrefsFragmentFirst.BUNDLE_KEY_OF_SUMMARY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS),
                                context));
            }
        }
        setPreferenceScreen(screen);
    }

    private Preference createPreference(final String key,
                                        final String title,
                                        final String summaryOfSrcPreference,
                                        final Context context) {
        final Preference preference = new Preference(context);
        preference.setKey(key);
        preference.setTitle(title);
        preference.setSummary("copied summary: " + summaryOfSrcPreference);
        return preference;
    }
}
