package de.KnollFrank.settingssearch.preference.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public class PreferenceFragmentWithSinglePreference extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        final Context context = getPreferenceManager().getContext();
        final PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);
        screen.addPreference(
                createPreference(
                        context,
                        requireArguments().getString(PrefsFragmentFirst.SUMMARY_OF_SRC_PREFERENCE)));
        setPreferenceScreen(screen);
    }

    private Preference createPreference(final Context context, final String summaryOfSrcPreference) {
        final Preference preference = new Preference(context);
        preference.setKey("keyOfPreferenceOfConnectedFragment");
        preference.setTitle("dst preference");
        preference.setSummary("copied summary: " + summaryOfSrcPreference);
        return preference;
    }
}
