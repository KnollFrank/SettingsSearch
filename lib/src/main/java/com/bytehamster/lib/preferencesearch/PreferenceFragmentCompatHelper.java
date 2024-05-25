package com.bytehamster.lib.preferencesearch;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;

public class PreferenceFragmentCompatHelper {

    private final FragmentActivity fragmentActivity;
    private final int containerResId;

    public PreferenceFragmentCompatHelper(final FragmentActivity fragmentActivity, @IdRes int containerResId) {
        this.fragmentActivity = fragmentActivity;
        this.containerResId = containerResId;
    }

    public PreferenceScreenWithHost getPreferenceScreenOfFragment(final String fragment) {
        final PreferenceFragmentCompat preferenceFragmentCompat =
                (PreferenceFragmentCompat) Fragment.instantiate(
                        this.fragmentActivity,
                        fragment,
                        null);
        initialize(preferenceFragmentCompat);
        return PreferenceScreenWithHostFactory.createPreferenceScreenWithHost(preferenceFragmentCompat);
    }

    public void initialize(final Fragment fragment) {
        this
                .fragmentActivity
                .getSupportFragmentManager()
                .beginTransaction()
                // FK-TODO: umsetzen: "Note: It is strongly recommended to always use fragment operations that take a Class rather than a fragment instance to ensure that the same mechanisms for creating the fragment are also used for restoring the fragment from a saved state. See Fragment manager for more details." (https://developer.android.com/guide/fragments/transactions)
                .replace(this.containerResId, fragment)
                .commitNow();
    }
}
