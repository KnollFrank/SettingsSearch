package com.bytehamster.lib.preferencesearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.bytehamster.lib.preferencesearch.common.Lists;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class PreferenceItems1 {

    private final PreferenceParser preferenceParser;

    public PreferenceItems1(final PreferenceParser preferenceParser) {
        this.preferenceParser = preferenceParser;
    }

    public List<PreferenceItem> getPreferenceItems(final Set<Class<? extends PreferenceFragmentCompat>> preferenceFragments) {
        final List<List<PreferenceItem>> preferenceItems =
                preferenceFragments
                        .stream()
                        .map(this::parsePreferenceScreen)
                        .collect(Collectors.toList());
        return Lists.concat(preferenceItems);
    }

    private List<PreferenceItem> parsePreferenceScreen(final Class<? extends PreferenceFragmentCompat> preferenceScreen) {
        final List<Preference> preferences = preferenceParser.parsePreferenceScreen(preferenceScreen);
        final List<Preference> searchablePreferences =
                PreferenceItemFilter.getSearchablePreferences(preferences);
        return PreferenceItems.getPreferenceItems(searchablePreferences, preferenceScreen);
    }
}
