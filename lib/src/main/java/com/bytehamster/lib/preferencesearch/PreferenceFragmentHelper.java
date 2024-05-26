package com.bytehamster.lib.preferencesearch;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

public class PreferenceFragmentHelper {

    private final FragmentActivity fragmentActivity;
    private final int containerResId;

    public PreferenceFragmentHelper(final FragmentActivity fragmentActivity, @IdRes int containerResId) {
        this.fragmentActivity = fragmentActivity;
        this.containerResId = containerResId;
    }

    public Optional<PreferenceScreenWithHost> getPreferenceScreenOfFragment(final String fragment) {
        return getPreferenceScreenOfFragment(Fragment.instantiate(this.fragmentActivity, fragment, null));
    }

    private Optional<PreferenceScreenWithHost> getPreferenceScreenOfFragment(final Fragment fragment) {
        if (!(fragment instanceof PreferenceFragmentCompat)) {
            return Optional.empty();
        }
        final PreferenceFragmentCompat preferenceFragment = (PreferenceFragmentCompat) fragment;
        initialize(preferenceFragment);
        return Optional.of(PreferenceScreenWithHostFactory.createPreferenceScreenWithHost(preferenceFragment));
    }

    public void initialize(final Fragment fragment) {
        this
                .fragmentActivity
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(this.containerResId, fragment)
                .commitNow();
    }
}
