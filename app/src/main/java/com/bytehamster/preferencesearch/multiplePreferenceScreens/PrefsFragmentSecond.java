package com.bytehamster.preferencesearch.multiplePreferenceScreens;

import android.os.Bundle;

import com.bytehamster.lib.preferencesearch.BaseSearchPreferenceFragment;
import com.bytehamster.preferencesearch.R;

public class PrefsFragmentSecond extends BaseSearchPreferenceFragment {

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.preferences2);
    }
}
