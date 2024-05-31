package com.bytehamster.lib.preferencesearch;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.bytehamster.lib.preferencesearch.common.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class PreferenceItems {

    public static List<PreferenceItem> getPreferenceItems(final Set<Class<? extends PreferenceFragmentCompat>> preferenceFragments,
                                                          final PreferenceProvider preferenceProvider) {
        class _PreferenceItems {

            private final PreferenceProvider preferenceProvider;

            public _PreferenceItems(final PreferenceProvider preferenceProvider) {
                this.preferenceProvider = preferenceProvider;
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
                final List<Preference> preferences = preferenceProvider.getPreferences(preferenceScreen);
                return PreferenceItems.getPreferenceItems(preferences, preferenceScreen);
            }
        }

        return new _PreferenceItems(preferenceProvider).getPreferenceItems(preferenceFragments);
    }

    public static List<PreferenceItem> getPreferenceItems(final List<Preference> preferences,
                                                          final Class<? extends PreferenceFragmentCompat> resId) {
        class _PreferenceItems {

            public static List<PreferenceItem> getPreferenceItems(final List<Preference> preferences,
                                                                  final Class<? extends PreferenceFragmentCompat> resId) {
                return preferences
                        .stream()
                        .map(preference -> getPreferenceItem(preference, resId))
                        .collect(Collectors.toList());
            }

            private static PreferenceItem getPreferenceItem(final Preference preference,
                                                            final Class<? extends PreferenceFragmentCompat> resId) {
                final PreferenceItem preferenceItem =
                        new PreferenceItem(
                                preference.getTitle() != null ? preference.getTitle().toString() : null,
                                preference.getSummary() != null ? preference.getSummary().toString() : null,
                                preference.getKey(),
                                null,
                                null,
                                resId);
                if (preference instanceof ListPreference) {
                    final ListPreference listPreference = ((ListPreference) preference);
                    if (listPreference.getEntries() != null) {
                        preferenceItem.entries = Arrays.toString(listPreference.getEntries());
                    }
                }
                return preferenceItem;
            }
        }

        return _PreferenceItems.getPreferenceItems(preferences, resId);
    }
}
