package com.bytehamster.lib.preferencesearch;

import android.content.Context;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentManager;

class PreferenceProviderFactory {

    public static PreferenceProvider createPreferenceProvider(final Context context,
                                                              final FragmentManager fragmentManager,
                                                              @IdRes final int containerResId) {
        return new PreferenceProvider(new PreferenceFragments(context, fragmentManager, containerResId));
    }
}
