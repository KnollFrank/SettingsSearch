package com.bytehamster.lib.preferencesearch;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;

class PreferenceParserFactory {

    public static PreferenceParser createPreferenceParser(final FragmentActivity fragmentActivity,
                                                          @IdRes final int containerResId) {
        return new PreferenceParser(new PreferenceFragments(fragmentActivity, containerResId));
    }
}
