package com.bytehamster.lib.preferencesearch;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;

public class PreferenceFragmentHelper {

    private final FragmentActivity fragmentActivity;
    private final int containerResId;

    public PreferenceFragmentHelper(final FragmentActivity fragmentActivity, @IdRes int containerResId) {
        this.fragmentActivity = fragmentActivity;
        this.containerResId = containerResId;
    }

    public PreferenceScreenWithHost getPreferenceScreenOfFragment(final String fragment) {
        final PreferenceFragmentCompat preferenceFragment =
                (PreferenceFragmentCompat) Fragment.instantiate(
                        this.fragmentActivity,
                        fragment,
                        null);
        initialize(preferenceFragment);
        return PreferenceScreenWithHostFactory.createPreferenceScreenWithHost(preferenceFragment);
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
