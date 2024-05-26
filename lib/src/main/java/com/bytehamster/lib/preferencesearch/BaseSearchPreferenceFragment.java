package com.bytehamster.lib.preferencesearch;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

import com.bytehamster.lib.preferencesearch.common.BundleHelper;

import java.util.Optional;

public abstract class BaseSearchPreferenceFragment extends PreferenceFragmentCompat {

    public static final String KEY_OF_PREFERENCE_2_HIGHLIGHT = "keyOfPreference2Highlight";

    private Optional<String> keyOfPreference2Highlight;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.keyOfPreference2Highlight = new BundleHelper(getArguments()).getString(KEY_OF_PREFERENCE_2_HIGHLIGHT);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.keyOfPreference2Highlight.ifPresent(
                keyOfPreference2Highlight -> {
                    final SearchPreferenceResult searchPreferenceResult = new SearchPreferenceResult(keyOfPreference2Highlight, null, null);
                    scrollToPreference(keyOfPreference2Highlight);
                    searchPreferenceResult.highlight(this);
                });
    }
}
