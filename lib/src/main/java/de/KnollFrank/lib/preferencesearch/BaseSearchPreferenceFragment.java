package de.KnollFrank.lib.preferencesearch;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

import org.threeten.bp.Duration;

import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.common.Bundles;

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
        // FK-TODO: was soll passieren, falls die Preference keinen key hat?
        keyOfPreference2Highlight.ifPresent(
                keyOfPreference2Highlight -> {
                    scrollToPreference(keyOfPreference2Highlight);
                    PreferenceHighlighter.highlightPreferenceOfPreferenceFragment(
                            keyOfPreference2Highlight,
                            this,
                            Duration.ofSeconds(1));
                });
    }
}
