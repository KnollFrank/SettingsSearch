package de.KnollFrank.lib.preferencesearch;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

public class SearchConfiguration {

    public final Optional<FragmentActivity> activity;
    @IdRes
    public final int fragmentContainerViewId;
    public final Optional<String> textHint;
    public final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment;

    public SearchConfiguration(final Optional<FragmentActivity> activity,
                               final @IdRes int fragmentContainerViewId,
                               final Optional<String> textHint,
                               final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment) {
        if (activity.isPresent() && !(activity.get() instanceof SearchPreferenceResultListener)) {
            throw new IllegalArgumentException("Activity must implement SearchPreferenceResultListener");
        }
        this.activity = activity;
        this.fragmentContainerViewId = fragmentContainerViewId;
        this.textHint = textHint;
        this.rootPreferenceFragment = rootPreferenceFragment;
    }
}
