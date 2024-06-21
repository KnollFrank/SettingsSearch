package de.KnollFrank.lib.preferencesearch.client;

import androidx.annotation.IdRes;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

public class SearchConfiguration {

    public final @IdRes int fragmentContainerViewId;
    public final Optional<String> textHint;
    public final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment;

    public SearchConfiguration(final @IdRes int fragmentContainerViewId,
                               final Optional<String> textHint,
                               final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment) {
        this.fragmentContainerViewId = fragmentContainerViewId;
        this.textHint = textHint;
        this.rootPreferenceFragment = rootPreferenceFragment;
    }
}
