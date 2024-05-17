package com.bytehamster.lib.preferencesearch;

import android.content.Context;

import androidx.annotation.XmlRes;
import androidx.preference.Preference;

import com.bytehamster.lib.preferencesearch.SearchConfiguration.SearchIndexItem;
import com.bytehamster.lib.preferencesearch.common.Utils;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.stream.Collectors;

class PreferenceItems {

    public static List<PreferenceItem> getPreferenceItems(final List<Preference> preferences,
                                                          @XmlRes final int resId) {
        return new SearchConfiguration().indexItems(preferences, resId);
    }

    public static List<PreferenceItem> getPreferenceItems(final SearchConfiguration searchConfiguration,
                                                          final Context context) {
        return ImmutableList
                .<PreferenceItem>builder()
                .addAll(parsePreferenceScreens(getPreferenceScreens(searchConfiguration), context))
                .addAll(searchConfiguration.getPreferencesToIndex())
                .build();
    }

    private static List<Integer> getPreferenceScreens(final SearchConfiguration searchConfiguration) {
        return searchConfiguration
                .getFiles()
                .stream()
                .map(SearchIndexItem::getResId)
                .collect(Collectors.toList());
    }

    private static List<PreferenceItem> parsePreferenceScreens(final List<Integer> preferenceScreens,
                                                               final Context context) {
        final List<List<PreferenceItem>> preferenceItems =
                preferenceScreens
                        .stream()
                        .map(preferenceScreen -> parsePreferenceScreen(preferenceScreen, context))
                        .collect(Collectors.toList());
        return Utils.concat(preferenceItems);
    }

    private static List<PreferenceItem> parsePreferenceScreen(@XmlRes final int preferenceScreen,
                                                              final Context context) {
        final List<Preference> preferences =
                PreferenceParser
                        .fromContext(context)
                        .parsePreferenceScreen(preferenceScreen);
        final List<Preference> searchablePreferences =
                PreferenceItemFilter.getSearchablePreferences(preferences);
        return getPreferenceItems(searchablePreferences, preferenceScreen);
    }
}
