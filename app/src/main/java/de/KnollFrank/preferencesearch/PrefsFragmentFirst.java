package de.KnollFrank.preferencesearch;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class PrefsFragmentFirst extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.preferences_multiple_screens);
    }
}
