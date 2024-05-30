package com.bytehamster.lib.preferencesearch;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.List;

class PreferenceItems {

    public static List<PreferenceItem> getPreferenceItems(final SearchConfiguration searchConfiguration,
                                                          final FragmentActivity fragmentActivity,
                                                          @IdRes final int containerResId) {
        return PreferenceItems1.getPreferenceItems(searchConfiguration, fragmentActivity, containerResId);
    }

    static List<PreferenceItem> getPreferenceItems(final List<Preference> preferences,
                                                   final Class<? extends PreferenceFragmentCompat> resId) {
        return PreferenceItems2.getPreferenceItems(preferences, resId);
    }
}
