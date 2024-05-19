package com.bytehamster.preferencesearch.multiplePreferenceScreens;

import static com.bytehamster.preferencesearch.multiplePreferenceScreens.MultiplePreferenceScreensExample.KEY_OF_PREFERENCE_2_HIGHLIGHT;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

import com.bytehamster.lib.preferencesearch.SearchPreferenceResult;

public abstract class BaseFragment extends PreferenceFragmentCompat {

    private String keyOfPreference2Highlight;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle arguments = getArguments();
        this.keyOfPreference2Highlight = arguments != null ? arguments.getString(KEY_OF_PREFERENCE_2_HIGHLIGHT) : null;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (this.keyOfPreference2Highlight != null) {
            final SearchPreferenceResult searchPreferenceResult = new SearchPreferenceResult(this.keyOfPreference2Highlight, 0, null);
            scrollToPreference(this.keyOfPreference2Highlight);
            searchPreferenceResult.highlight(this);
        }
    }
}
