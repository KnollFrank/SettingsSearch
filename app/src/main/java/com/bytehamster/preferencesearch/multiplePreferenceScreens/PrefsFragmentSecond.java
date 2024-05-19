package com.bytehamster.preferencesearch.multiplePreferenceScreens;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

import com.bytehamster.lib.preferencesearch.SearchPreferenceResult;
import com.bytehamster.preferencesearch.R;

public class PrefsFragmentSecond extends PreferenceFragmentCompat {

    private String keyOfPreference2Highlight;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle arguments = getArguments();
        this.keyOfPreference2Highlight = arguments.getString(MultiplePreferenceScreensExample.KEY_OF_PREFERENCE_2_HIGHLIGHT);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (this.keyOfPreference2Highlight != null) {
            final SearchPreferenceResult searchPreferenceResult = new SearchPreferenceResult(keyOfPreference2Highlight, 0, null);
            searchPreferenceResult.highlight(this);
        }
    }

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.preferences2);
    }
}
