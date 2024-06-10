package de.KnollFrank.lib.preferencesearch;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

public class SearchConfiguration {

    // FK-TODO: make activity non Optional
    public final Optional<FragmentActivity> activity;
    public final @IdRes int fragmentContainerViewId;
    public final Optional<String> textHint;
    public final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment;

    public SearchConfiguration(final Optional<FragmentActivity> activity,
                               final @IdRes int fragmentContainerViewId,
                               final Optional<String> textHint,
                               final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment) {
        this.activity = activity;
        this.fragmentContainerViewId = fragmentContainerViewId;
        this.textHint = textHint;
        this.rootPreferenceFragment = rootPreferenceFragment;
    }
}
