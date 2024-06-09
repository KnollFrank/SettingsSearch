package de.KnollFrank.lib.preferencesearch;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.common.Bundles;

// FK-TODO: replace this base class with a Lifecycle-Aware Component? see https://developer.android.com/topic/libraries/architecture/lifecycle
public abstract class BaseSearchPreferenceFragment extends PreferenceFragmentCompat {

    public static final String KEY_OF_PREFERENCE_2_HIGHLIGHT = "keyOfPreference2Highlight";

    private Optional<String> keyOfPreference2Highlight;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        keyOfPreference2Highlight =
                getArguments() != null ?
                        new Bundles(getArguments()).getOptionalString(KEY_OF_PREFERENCE_2_HIGHLIGHT) :
                        Optional.empty();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        keyOfPreference2Highlight.ifPresent(
                keyOfPreference2Highlight -> {
                    final SearchPreferenceResult searchPreferenceResult =
                            new SearchPreferenceResult(
                                    keyOfPreference2Highlight,
                                    null);
                    scrollToPreference(keyOfPreference2Highlight);
                    searchPreferenceResult.highlight(this);
                });
    }
}
