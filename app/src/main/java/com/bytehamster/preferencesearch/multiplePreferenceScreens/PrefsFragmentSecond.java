package com.bytehamster.preferencesearch.multiplePreferenceScreens;

import android.os.Bundle;

import com.bytehamster.lib.preferencesearch.BaseFragment;
import com.bytehamster.preferencesearch.R;

public class PrefsFragmentSecond extends BaseFragment {

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.preferences2);
    }
}
