package com.bytehamster.lib.preferencesearch;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.List;
import java.util.Set;

class PreferenceItems {

    public static List<PreferenceItem> getPreferenceItems(final Set<Class<? extends PreferenceFragmentCompat>> preferenceFragments,
                                                          final FragmentActivity fragmentActivity,
                                                          @IdRes final int containerResId) {
        return PreferenceItems1.getPreferenceItems(preferenceFragments, fragmentActivity, containerResId);
    }

    static List<PreferenceItem> getPreferenceItems(final List<Preference> preferences,
                                                   final Class<? extends PreferenceFragmentCompat> resId) {
        return PreferenceItems2.getPreferenceItems(preferences, resId);
    }
}
