package com.bytehamster.preferencesearch;

import android.os.Bundle;

import com.bytehamster.lib.preferencesearch.BaseSearchPreferenceFragment;

public class PrefsFragmentThird extends BaseSearchPreferenceFragment {

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.preferences3);
    }
}