package com.bytehamster.lib.preferencesearch;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.bytehamster.lib.preferencesearch.common.Lists;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class PreferenceItems1 {

    public static List<PreferenceItem> getPreferenceItems(
            final Set<Class<? extends PreferenceFragmentCompat>> preferenceFragments,
            // FK-TODO: make fragmentActivity and containerResId constructor params
            final FragmentActivity fragmentActivity,
            @IdRes final int containerResId) {
        final List<List<PreferenceItem>> preferenceItems =
                preferenceFragments
                        .stream()
                        .map(preferenceScreen -> parsePreferenceScreen(preferenceScreen, fragmentActivity, containerResId))
                        .collect(Collectors.toList());
        return Lists.concat(preferenceItems);
    }

    private static List<PreferenceItem> parsePreferenceScreen(
            final Class<? extends PreferenceFragmentCompat> preferenceScreen,
            final FragmentActivity fragmentActivity,
            @IdRes final int containerResId) {
        final List<Preference> preferences =
                new PreferenceParser(new PreferenceFragments(fragmentActivity, containerResId))
                        .parsePreferenceScreen(preferenceScreen);
        final List<Preference> searchablePreferences =
                PreferenceItemFilter.getSearchablePreferences(preferences);
        return PreferenceItems.getPreferenceItems(searchablePreferences, preferenceScreen);
    }
}
