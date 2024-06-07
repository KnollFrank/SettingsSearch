package de.KnollFrank.lib.preferencesearch;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.common.Lists;

public class PreferenceItems {

    public static List<PreferenceItem> getPreferenceItems(
            final Set<Class<? extends PreferenceFragmentCompat>> preferenceFragments,
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

    public static List<PreferenceItem> getPreferenceItems(
            final List<Preference> preferences,
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
                return new PreferenceItem(
                        asString(preference.getTitle()),
                        asString(preference.getSummary()),
                        Optional.ofNullable(preference.getKey()),
                        Optional.empty(),
                        Optional.empty(),
                        getEntries(preference),
                        resId);
            }

            private static Optional<String> asString(final CharSequence charSequence) {
                return Optional
                        .ofNullable(charSequence)
                        .map(CharSequence::toString);
            }

            private static Optional<String> getEntries(final Preference preference) {
                if (!(preference instanceof ListPreference)) {
                    return Optional.empty();
                }
                return Optional
                        .ofNullable(((ListPreference) preference).getEntries())
                        .map(Arrays::toString);
            }
        }

        return _PreferenceItems.getPreferenceItems(preferences, resId);
    }
}
