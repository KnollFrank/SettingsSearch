package com.bytehamster.preferencesearch.multiplePreferenceScreens;

import android.os.Bundle;

import com.bytehamster.preferencesearch.R;

public class PrefsFragmentFourth extends BaseFragment {

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.preferences4);
    }
}
