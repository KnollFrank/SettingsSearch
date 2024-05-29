package com.bytehamster.lib.preferencesearch;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

public class PreferenceFragments {

    private final FragmentActivity fragmentActivity;
    private final int containerResId;

    public PreferenceFragments(final FragmentActivity fragmentActivity, @IdRes int containerResId) {
        this.fragmentActivity = fragmentActivity;
        this.containerResId = containerResId;
    }

    public Optional<PreferenceScreenWithHost> getPreferenceScreenOfFragment(final String fragment) {
        return getPreferenceScreenOfFragment(Fragment.instantiate(this.fragmentActivity, fragment, null));
    }

    public void initialize(final Fragment fragment) {
        this
                .fragmentActivity
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(this.containerResId, fragment)
                .commitNow();
    }

    private Optional<PreferenceScreenWithHost> getPreferenceScreenOfFragment(final Fragment fragment) {
        return fragment instanceof PreferenceFragmentCompat ?
                Optional.of(getPreferenceScreenOfFragment((PreferenceFragmentCompat) fragment)) :
                Optional.empty();
    }

    private PreferenceScreenWithHost getPreferenceScreenOfFragment(final PreferenceFragmentCompat preferenceFragment) {
        initialize(preferenceFragment);
        return PreferenceScreenWithHostFactory.createPreferenceScreenWithHost(preferenceFragment);
    }
}
