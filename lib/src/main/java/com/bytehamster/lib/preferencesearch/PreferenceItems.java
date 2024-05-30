package com.bytehamster.lib.preferencesearch;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.bytehamster.lib.preferencesearch.common.Lists;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.stream.Collectors;

class PreferenceItems {

    public static List<PreferenceItem> getPreferenceItems(final List<Preference> preferences,
                                                          final Class<? extends PreferenceFragmentCompat> resId) {
        return new SearchConfiguration().indexItems(preferences, resId);
    }

    public static List<PreferenceItem> getPreferenceItems(final SearchConfiguration searchConfiguration,
                                                          final FragmentActivity fragmentActivity,
                                                          @IdRes final int containerResId) {
        return ImmutableList
                .<PreferenceItem>builder()
                .addAll(parsePreferenceScreens(getPreferenceScreens(searchConfiguration), fragmentActivity, containerResId))
                .build();
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
        return getPreferenceItems(searchablePreferences, preferenceScreen);
    }
}
