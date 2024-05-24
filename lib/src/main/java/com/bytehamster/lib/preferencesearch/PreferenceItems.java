package com.bytehamster.lib.preferencesearch;

import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.bytehamster.lib.preferencesearch.common.Utils;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.stream.Collectors;

class PreferenceItems {

    public static List<PreferenceItem> getPreferenceItems(final List<Preference> preferences,
                                                          final Class<? extends PreferenceFragmentCompat> resId) {
        return new SearchConfiguration().indexItems(preferences, resId);
    }

    public static List<PreferenceItem> getPreferenceItems(final SearchConfiguration searchConfiguration,
                                                          final FragmentActivity fragmentActivity) {
        return ImmutableList
                .<PreferenceItem>builder()
                .addAll(parsePreferenceScreens(getPreferenceScreens(searchConfiguration), fragmentActivity))
                .addAll(searchConfiguration.getPreferencesToIndex())
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
                                                               final FragmentActivity fragmentActivity) {
        final List<List<PreferenceItem>> preferenceItems =
                preferenceScreens
                        .stream()
                        .map(preferenceScreen -> parsePreferenceScreen(preferenceScreen, fragmentActivity))
                        .collect(Collectors.toList());
        return Utils.concat(preferenceItems);
    }

    private static List<PreferenceItem> parsePreferenceScreen(
            final Class<? extends PreferenceFragmentCompat> preferenceScreen,
            final FragmentActivity fragmentActivity) {
        final List<Preference> preferences =
                new PreferenceParser(new PreferenceFragmentCompatHelper(fragmentActivity))
                        .parsePreferenceScreen(preferenceScreen);
        final List<Preference> searchablePreferences =
                PreferenceItemFilter.getSearchablePreferences(preferences);
        return getPreferenceItems(searchablePreferences, preferenceScreen);
    }
}
