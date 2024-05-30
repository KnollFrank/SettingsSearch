package com.bytehamster.lib.preferencesearch;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.bytehamster.lib.preferencesearch.common.Lists;

import java.util.List;
import java.util.stream.Collectors;

class PreferenceItems1 {

    public static List<PreferenceItem> getPreferenceItems(
            final SearchConfiguration searchConfiguration,
            final FragmentActivity fragmentActivity,
            @IdRes final int containerResId) {
        return parsePreferenceScreens(
                getPreferenceScreens(searchConfiguration),
                fragmentActivity,
                containerResId);
    }

    private static List<Class<? extends PreferenceFragmentCompat>> getPreferenceScreens(final SearchConfiguration searchConfiguration) {
        return searchConfiguration
                .getFiles()
                .stream()
                .map(SearchIndexItem::getResId)
                .collect(Collectors.toList());
    }

    private static List<PreferenceItem> parsePreferenceScreens(final List<Class<? extends PreferenceFragmentCompat>> preferenceScreens,
                                                               final FragmentActivity fragmentActivity,
                                                               @IdRes final int containerResId) {
        final List<List<PreferenceItem>> preferenceItems =
                preferenceScreens
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
