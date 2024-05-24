package com.bytehamster.lib.preferencesearch;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;

public class PreferenceFragmentCompatHelper {

    private final FragmentActivity fragmentActivity;

    public PreferenceFragmentCompatHelper(final FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
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
                .replace(android.R.id.content, fragment)
                .commitNow();
    }
}
