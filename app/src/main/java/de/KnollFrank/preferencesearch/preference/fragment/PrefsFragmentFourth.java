package de.KnollFrank.preferencesearch.preference.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.preferencesearch.R;

public class PrefsFragmentFourth extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.preferences4);
    }
}
