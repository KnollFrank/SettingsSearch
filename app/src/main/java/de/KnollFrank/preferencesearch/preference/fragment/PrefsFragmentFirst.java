package de.KnollFrank.preferencesearch.preference.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.preferencesearch.R;

public class PrefsFragmentFirst extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.preferences_multiple_screens);
    }
}
