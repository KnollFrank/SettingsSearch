package com.bytehamster.lib.preferencesearch;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;

class PreferenceProviderFactory {

    public static PreferenceProvider createPreferenceProvider(final FragmentActivity fragmentActivity,
                                                              @IdRes final int containerResId) {
        return new PreferenceProvider(new PreferenceFragments(fragmentActivity, containerResId));
    }
}
